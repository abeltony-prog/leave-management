#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print section headers
print_section() {
    echo -e "\n${BLUE}=== $1 ===${NC}"
}

# Function to handle errors
handle_error() {
    echo -e "${RED}Error: $1${NC}"
    exit 1
}

echo -e "${YELLOW}Starting repository cleanup...${NC}"

# Check if git is installed
if ! command -v git &> /dev/null; then
    handle_error "Git is not installed. Please install Git first."
fi

# Check if git-lfs is installed
if ! command -v git-lfs &> /dev/null; then
    handle_error "Git LFS is not installed. Please install Git LFS first."
fi

# Initialize Git LFS if not already initialized
if [ ! -f .gitattributes ]; then
    print_section "Initializing Git LFS"
    git lfs install
    git lfs track "*.jar" "*.war" "*.zip" "*.tar.gz" "*.rar"
    git add .gitattributes
fi

# Remove large files from git history
print_section "Removing Large Files"
git rm --cached target/*.jar
git rm --cached target/*.war
git rm --cached target/*.zip
git rm --cached target/*.tar.gz
git rm --cached target/*.rar

# Clean up build artifacts
print_section "Cleaning Build Artifacts"
rm -rf target/
rm -rf build/
rm -rf out/
rm -rf .gradle/
rm -rf .mvn/wrapper/maven-wrapper.jar

# Clean up dependency caches
print_section "Cleaning Dependency Caches"
rm -rf ~/.m2/repository/com/africahr/
rm -rf node_modules/
rm -rf .npm/
rm -rf .yarn/

# Clean up application logs and temp files
print_section "Cleaning Application Files"
rm -rf logs/
rm -rf *.log
rm -rf *.tmp
rm -rf *.bak
rm -rf *.swp
rm -rf .DS_Store
rm -rf Thumbs.db

# Clean up IDE and editor files
print_section "Cleaning IDE Files"
rm -rf .idea/
rm -rf .vscode/
rm -rf *.iml
rm -rf *.ipr
rm -rf *.iws
rm -rf .classpath
rm -rf .project
rm -rf .settings/

# Clean up environment files
print_section "Cleaning Environment Files"
rm -rf .env
rm -rf .env.local
rm -rf .env.development
rm -rf .env.production

# Clean up Docker files
print_section "Cleaning Docker Files"
rm -rf docker-compose.override.yml
rm -rf .docker/

# Clean up database files
print_section "Cleaning Database Files"
rm -rf src/main/resources/db/changelog/changes/*.xml
rm -rf src/main/resources/db/changelog/*.xml
rm -rf .hibernate/

# Clean up untracked files
print_section "Cleaning Untracked Files"
git clean -fd

# Remove empty directories
print_section "Removing Empty Directories"
find . -type d -empty -delete

# Update .gitignore
print_section "Updating .gitignore"
cat > .gitignore << EOL
# Compiled class file
*.class

# Log file
*.log

# Package Files #
*.jar
*.war
*.nar
*.ear
*.zip
*.tar.gz
*.rar

# Maven
target/
!.mvn/wrapper/maven-wrapper.jar
!**/src/main/**/target/
!**/src/test/**/target/

# Gradle
.gradle/
build/
out/

# Node
node_modules/
dist/
build/
.npm/
.yarn/

# IDE
.idea/
.vscode/
*.iml
*.ipr
*.iws
.classpath
.project
.settings/

# System files
.DS_Store
Thumbs.db
ehthumbs.db
Desktop.ini

# Environment
.env
.env.local
.env.development
.env.production

# Docker
docker-compose.override.yml
.docker/

# Database
src/main/resources/db/changelog/changes/*.xml
src/main/resources/db/changelog/*.xml
.hibernate/

# Temporary files
*.tmp
*.bak
*.swp
*~.nib
local.properties
EOL

# Commit changes
print_section "Committing Changes"
git add .
git commit -m "Cleanup: Remove unnecessary files and update .gitignore"

echo -e "\n${GREEN}Cleanup completed successfully!${NC}"
echo -e "${YELLOW}Don't forget to push your changes:${NC}"
echo -e "git push origin main" 