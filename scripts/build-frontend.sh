#!/bin/bash

set -euo pipefail

RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m'

die() { echo -e "${RED}$1${NC}"; exit 1; }
ok() { echo -e "${GREEN}$1${NC}"; }

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
cd "$PROJECT_ROOT"

command -v node >/dev/null 2>&1 || die "Node.js not found"
command -v npm >/dev/null 2>&1 || die "npm not found"

cd frontend

# npm ci is used to install dependencies without updating them, instead of npm install
npm ci --no-audit --no-fund --loglevel=error || die "Failed to install dependencies"

npm run build || die "Failed to build frontend"

[ -d dist ] || die "Dist directory not found"

# grep -q "/new_front/" dist/index.html || die "index.html does not contain /new_front/"

ok "Build frontend ended"
