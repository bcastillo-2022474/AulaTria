// Exists only so the root magefile can compile. Nothing else in the repo is Go
// (the API is TypeScript); mage is the task runner, as in Chassis.
module github.com/aulatria/tools

go 1.26.7

require github.com/magefile/mage v1.15.0
