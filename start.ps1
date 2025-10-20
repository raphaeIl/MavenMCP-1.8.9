# Build and launch Minecraft MCP
# This script builds the jar, copies it to test_run, and launches Minecraft

param(
    [string]$ProjectRoot = "E:\documents\Decompiling\Extracted\Minecraft\MavenMCP-1.8.9",
    [string]$TestRunDir = "E:\documents\Decompiling\Extracted\Minecraft\MavenMCP-1.8.9\test_run",
    [string]$JarName = "MCP-1.8.9-jar-with-dependencies.jar"
)

Write-Host "Building Minecraft MCP..." -ForegroundColor Green

# Change to project directory
Set-Location $ProjectRoot

# Run Maven build
Write-Host "Running mvn clean package..." -ForegroundColor Yellow
mvn clean package

if ($LASTEXITCODE -ne 0) {
    Write-Host "Maven build failed!" -ForegroundColor Red
    exit 1
}

# Define paths
$SourceJar = Join-Path $ProjectRoot "target\$JarName"
$VersionsDir = Join-Path $TestRunDir "versions\1.8.9"
$TargetJar = Join-Path $VersionsDir "1.8.9.jar"

# Create versions directory if it doesn't exist
if (!(Test-Path $VersionsDir)) {
    New-Item -ItemType Directory -Path $VersionsDir -Force
    Write-Host "Created versions directory: $VersionsDir" -ForegroundColor Yellow
}

# Copy the built jar
if (Test-Path $SourceJar) {
    Write-Host "Copying $JarName to test_run/versions/1.8.9/1.8.9.jar..." -ForegroundColor Yellow
    Copy-Item $SourceJar $TargetJar -Force
    Write-Host "Jar copied successfully!" -ForegroundColor Green
} else {
    Write-Host "Built jar not found at: $SourceJar" -ForegroundColor Red
    exit 1
}

# Launch Minecraft
Write-Host "Launching Minecraft..." -ForegroundColor Green
Set-Location $TestRunDir
java -Dorg.lwjgl.librarypath="$ProjectRoot\test_natives\windows" -cp "versions\1.8.9\1.8.9.jar" Start