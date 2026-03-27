# Generate a 3-link certificate chain (Root -> Intermediate -> Server) with student id 22017
# Outputs:
# - public certs in certs/public/
# - private keys + server keystore in certs/private/ (ignored by git)

$ErrorActionPreference = "Stop"

$debugLogPath = "C:\\Users\\Ivan\\Desktop\\RBPO\\debug-91533c.log"

function Debug-Ndjson($runId, $hypothesisId, $location, $message, $data) {
  $ts = [DateTimeOffset]::UtcNow.ToUnixTimeMilliseconds()
  $obj = [ordered]@{
    sessionId    = "91533c"
    runId        = $runId
    hypothesisId = $hypothesisId
    location     = $location
    message      = $message
    data         = $data
    timestamp    = $ts
  }
  try { Add-Content -Path $debugLogPath -Value ($obj | ConvertTo-Json -Compress) -Encoding utf8 } catch {}
}

$projRoot = Split-Path -Parent (Split-Path -Parent $PSScriptRoot)
$pub = Join-Path $projRoot "certs\\public"
$priv = Join-Path $projRoot "certs\\private"

New-Item -ItemType Directory -Force -Path $pub | Out-Null
New-Item -ItemType Directory -Force -Path $priv | Out-Null

# #region agent log
Debug-Ndjson "run-tls-1" "H1" "generate-chain.ps1:projRoot" "paths" @{
  PSScriptRoot = "$PSScriptRoot"
  projRoot     = "$projRoot"
  pub          = "$pub"
  priv         = "$priv"
  pwd          = "$PWD"
}
# #endregion

function Require-OpenSSL {
  $cmd = Get-Command openssl -ErrorAction SilentlyContinue
  if (-not $cmd) {
    throw "OpenSSL not found in PATH. Install OpenSSL or use Git for Windows bash that includes openssl."
  }
}

Require-OpenSSL

# #region agent log
try {
  $v = ((& openssl version 2>&1) | ForEach-Object { $_.ToString() }) -join "`n"
  Debug-Ndjson "run-tls-1" "H4" "generate-chain.ps1:openssl" "openssl_version" @{ text = $v }
} catch {}
# #endregion

$studentOu = "OU=STUDENT-22017"
$rootSubj = "/C=RU/O=RBPO-AVIATION/CN=RBPO-RootCA-22017/$studentOu"
$intSubj  = "/C=RU/O=RBPO-AVIATION/CN=RBPO-IntermediateCA-22017/$studentOu"
$srvSubj  = "/C=RU/O=RBPO-AVIATION/CN=aviationservice-localhost-22017/$studentOu"

$rootKey = Join-Path $priv "root-ca-22017.key"
$rootCrt = Join-Path $pub  "root-ca-22017.crt"

$intKey  = Join-Path $priv "intermediate-ca-22017.key"
$intCsr  = Join-Path $priv "intermediate-ca-22017.csr"
$intCrt  = Join-Path $pub  "intermediate-ca-22017.crt"

$srvKey  = Join-Path $priv "server-22017.key"
$srvCsr  = Join-Path $priv "server-22017.csr"
$srvCrt  = Join-Path $pub  "server-22017.crt"
$srvChain = Join-Path $pub "server-22017-chain.crt"

$intExt = Join-Path $priv "intermediate-22017.ext"
$srvExt = Join-Path $priv "server-22017.ext"

$p12Out = Join-Path $priv "server-22017.p12"
$p12Pass = Read-Host -AsSecureString "Enter password for server-22017.p12"
$p12PassPlain = [Runtime.InteropServices.Marshal]::PtrToStringAuto([Runtime.InteropServices.Marshal]::SecureStringToBSTR($p12Pass))

# Root CA
$outRootKey = ((& openssl genrsa -out "$rootKey" 4096 2>&1) | ForEach-Object { $_.ToString() }) -join "`n"
$ecRootKey = $LASTEXITCODE
Debug-Ndjson "run-tls-1" "H5" "generate-chain.ps1:root_key" "openssl_genrsa" @{
  exitCode  = $ecRootKey
  keyExists = (Test-Path $rootKey)
  out       = $outRootKey
}
if ($ecRootKey -ne 0) { throw "OpenSSL failed to create root key" }

$outRootCrt = ((& openssl req -x509 -new -nodes -key "$rootKey" -sha256 -days 3650 -subj "$rootSubj" -out "$rootCrt" 2>&1) | ForEach-Object { $_.ToString() }) -join "`n"
$ecRootCrt = $LASTEXITCODE
Debug-Ndjson "run-tls-1" "H5" "generate-chain.ps1:root_crt" "openssl_req_x509" @{
  exitCode  = $ecRootCrt
  crtExists = (Test-Path $rootCrt)
  out       = $outRootCrt
}
if ($ecRootCrt -ne 0) { throw "OpenSSL failed to create root certificate" }

# Intermediate CA
openssl genrsa -out "$intKey" 4096 | Out-Null
# #region agent log
$outIntReq = ((& openssl req -new -key "$intKey" -subj "$intSubj" -out "$intCsr" 2>&1) | ForEach-Object { $_.ToString() }) -join "`n"
$ecIntReq = $LASTEXITCODE
Debug-Ndjson "run-tls-1" "H2" "generate-chain.ps1:int_req" "openssl_req" @{
  exitCode  = $ecIntReq
  csrExists = (Test-Path $intCsr)
  out       = $outIntReq
}
if ($ecIntReq -ne 0) { throw \"OpenSSL failed to create intermediate CSR\" }
# #endregion

@"
basicConstraints=CA:TRUE,pathlen:0
keyUsage=critical,keyCertSign,cRLSign
subjectKeyIdentifier=hash
authorityKeyIdentifier=keyid,issuer
"@ | Set-Content -Encoding ascii "$intExt"

openssl x509 -req -in "$intCsr" -CA "$rootCrt" -CAkey "$rootKey" -CAcreateserial -out "$intCrt" -days 1825 -sha256 -extfile "$intExt" | Out-Null

# Server cert for localhost
openssl genrsa -out "$srvKey" 2048 | Out-Null
# #region agent log
$outSrvReq = ((& openssl req -new -key "$srvKey" -subj "$srvSubj" -out "$srvCsr" 2>&1) | ForEach-Object { $_.ToString() }) -join "`n"
$ecSrvReq = $LASTEXITCODE
Debug-Ndjson "run-tls-1" "H3" "generate-chain.ps1:srv_req" "openssl_req" @{
  exitCode  = $ecSrvReq
  csrExists = (Test-Path $srvCsr)
  out       = $outSrvReq
}
if ($ecSrvReq -ne 0) { throw \"OpenSSL failed to create server CSR\" }
# #endregion

@"
basicConstraints=CA:FALSE
keyUsage=critical,digitalSignature,keyEncipherment
extendedKeyUsage=serverAuth
subjectAltName=@alt_names

[alt_names]
DNS.1=localhost
IP.1=127.0.0.1
"@ | Set-Content -Encoding ascii "$srvExt"

openssl x509 -req -in "$srvCsr" -CA "$intCrt" -CAkey "$intKey" -CAcreateserial -out "$srvCrt" -days 825 -sha256 -extfile "$srvExt" | Out-Null

# Chain file (server + intermediate + root)
Get-Content "$srvCrt","$intCrt","$rootCrt" | Set-Content -Encoding ascii "$srvChain"

# PKCS12 keystore for Spring Boot
openssl pkcs12 -export -out "$p12Out" -inkey "$srvKey" -in "$srvCrt" -certfile "$intCrt" -name "aviation-22017" -passout pass:$p12PassPlain | Out-Null

Write-Host ""
Write-Host "Done."
Write-Host "Public certs:"
Write-Host " - $rootCrt"
Write-Host " - $intCrt"
Write-Host " - $srvCrt"
Write-Host " - $srvChain"
Write-Host ""
Write-Host "Private (DO NOT COMMIT):"
Write-Host " - $p12Out"
Write-Host ""
Write-Host "Next: import Root CA into Windows trust store and set env vars to enable TLS."

