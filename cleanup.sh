#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Starting repository cleanup...${NC}"

# Check if git is installed
if ! command -v git &> /dev/null; then
    echo -e "${RED}Git is not installed. Please install Git first.${NC}"
    exit 1
fi

# Check if git-lfs is installed
if ! command -v git-lfs &> /dev/null; then
    echo -e "${RED}Git LFS is not installed. Please install Git LFS first.${NC}"
    exit 1
fi

# Initialize Git LFS if not already initialized
if [ ! -f .gitattributes ]; then
    echo -e "${YELLOW}Initializing Git LFS...${NC}"
    git lfs install
    git lfs track "*.jar" "*.war" "*.zip" "*.tar.gz" "*.rar"
    git add .gitattributes
fi

# Remove large files from git history
echo -e "${YELLOW}Removing large files from git history...${NC}"
git rm --cached target/*.jar
git rm --cached target/*.war
git rm --cached target/*.zip
git rm --cached target/*.tar.gz
git rm --cached target/*.rar

# Clean up untracked files
echo -e "${YELLOW}Cleaning up untracked files...${NC}"
git clean -fd

# Remove empty directories
echo -e "${YELLOW}Removing empty directories...${NC}"
find . -type d -empty -delete

# Update .gitignore if needed
if [ ! -f .gitignore ]; then
    echo -e "${YELLOW}Creating .gitignore file...${NC}"
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

# IDE
.idea/
.vscode/
*.iml
*.ipr
*.iws

# System files
.DS_Store
Thumbs.db

# Environment
.env
.env.local

# Temporary files
*.tmp
*.bak
*.swp
EOL
fi

# Commit changes
echo -e "${YELLOW}Committing changes...${NC}"
git add .
git commit -m "Cleanup: Remove large files and update .gitignore"

echo -e "${GREEN}Cleanup completed successfully!${NC}"
echo -e "${YELLOW}Don't forget to push your changes:${NC}"
echo -e "git push origin main" 