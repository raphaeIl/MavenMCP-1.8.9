# Launch Minecraft MCP (launch only - no compilation)
# This script launches the already compiled Minecraft server

param(
    [string]$ProjectRoot = "E:\documents\Decompiling\Extracted\Minecraft\MavenMCP-1.8.9",
    [string]$TestRunDir = "E:\documents\Decompiling\Extracted\Minecraft\MavenMCP-1.8.9\test_run",
    [string]$JarName = "1.8.9.jar"
)

Write-Host "Launching Minecraft MCP..." -ForegroundColor Green

# Define paths
$VersionsDir = Join-Path $TestRunDir "versions\1.8.9"
$TargetJar = Join-Path $VersionsDir $JarName

# Check if the jar exists
if (!(Test-Path $TargetJar)) {
    Write-Host "Jar not found at: $TargetJar" -ForegroundColor Red
    Write-Host "Please run start.ps1 first to compile and build the project." -ForegroundColor Yellow
    exit 1
}

# Change to test_run directory
Set-Location $TestRunDir

# Launch Minecraft
Write-Host "Starting Minecraft server..." -ForegroundColor Green
Write-Host "Using jar: $TargetJar" -ForegroundColor Cyan
Write-Host "Configuration will be loaded from: genconfig.json" -ForegroundColor Cyan

java -Dorg.lwjgl.librarypath="$ProjectRoot\test_natives\windows" -cp "versions\1.8.9\$JarName" Start
