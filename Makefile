#
# Grab the version of the project
#
ifeq ($(VERSION),)
VERSION := $(shell mvn -q -Dexec.executable="echo" -Dexec.args='$${project.version}' --non-recursive org.codehaus.mojo:exec-maven-plugin:1.3.1:exec)
endif

#
# The sceptre deployment environment to use
#
ifeq ($(ENV),)
ENV := dev
endif

#
# Used to set security group rules. Defaults to your specific IP and requires that curl is installed locally
#
ifeq ($(ALLOWED_IP_CIDR),)
ALLOWED_IP_CIDR := $(shell curl -s http://checkip.amazonaws.com)/32
endif

#
# The name of the template we'll be deploying
#
ifeq ($(TEMPLATE_NAME),)
TEMPLATE_NAME?=devsecops-stack
endif

#
# Check to ensure users have activated the python virtualenv before running sceptre commands
#
sceptre-exists: ; @which sceptre > /dev/null || echo "Run the following command to switch to deploy mode and then re-run your comand:\nsource deploy/bin/activate\n"

#
# Pass all the common args to every sceptre call to simplify
#
SCEPTRE_ARGS := --var "version=$(VERSION)" --var "allowed_ip_cidr=$(ALLOWED_IP_CIDR)" --dir "cloudformation"

.PHONY: help init all clean docs project create update delete outputs validate

help:
	@echo "\n--- Cloudformation orchestration targets ---"
	@echo "init:        Run once after cloning the project to intialize the deployment toolchain"
	@echo "create:      Create an application stack"
	@echo "update:      Update an application stack"
	@echo "delete:      Delete an application stack"
	@echo "outputs:     Display the stack outputs like the address to the load balancer"
	@echo "\n--- Development Targets ---"
	@echo "all:         Build the application and documentation"
	@echo "clean:       Remove all temporary build files"
	@echo "docs:        Build just the documentation"
	@echo "project:     Build just the project\n"

#
# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
# Project build targets
#

all:
	@./mvnw -P docs verify

clean:
	@./mvnw clean

docs:
	@./mvnw -P docs -pl docs package

project:
	@./mvnw verify

#
# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
# Cloudformation orchestration targets
#

#
# This is run once after the project is first checked out to intialize the deployment toolchain
#
init:
	pip install --upgrade virtualenv
	virtualenv deploy
	deploy/bin/pip install --upgrade sceptre awscli
	@echo "\nVirtual environemnt with Sceptre and AWS CLI installed successfully!"
	@echo "Run the command to activate: source deploy/bin/activate"

#
# Create an application stack
#
create: sceptre-exists
	@sceptre $(SCEPTRE_ARGS) create-stack $(ENV) $(TEMPLATE_NAME)

#
# Update a stack. If a version other than the current code is desired set the version ex: VERSION=4.0.0 make update
#
update: sceptre-exists
	@sceptre $(SCEPTRE_ARGS) update-stack $(ENV) $(TEMPLATE_NAME)

#
# Delete an application stack
#
delete: sceptre-exists
	@sceptre $(SCEPTRE_ARGS) delete-stack $(ENV) $(TEMPLATE_NAME)
	@echo "EC2 Container Registry"
	@aws ecr delete-repository --force --repository-name api-raptor-devops-dev-devsecops-stack

#
# Get the outputs from the stack. The BeanstalkEndpointURL contains the URL to the load balancer
#
outputs: sceptre-exists
	@sceptre $(SCEPTRE_ARGS) describe-stack-outputs $(ENV) $(TEMPLATE_NAME)

#
# Validate the Cloudformation main template
#
validate: sceptre-exists
	@sceptre $(SCEPTRE_ARGS) validate-template $(ENV) $(TEMPLATE_NAME)