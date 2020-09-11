# Copies the pre-commit hook script that will automatically update
# and add the vars README.md during a commit
cp ./scripts/pre-commit ./.git/hooks/pre-commit
chmod +x ./.git/hooks/pre-commit
