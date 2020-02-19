"# AWSAssignment" 



Serverless stack for SQS Queue, Lambda Function(triggered by queue) and DynamoDB

Prerequisite: 
	AWS CLI must be installed on your machine.

Steps to create stack using commands:

1. Open Comman prompt
2. Enter command "aws configure" and enter credentials, region
3. Go to file location of your stack template
4. Enter command "aws cloudformation create-stack --stack-name myteststack --template-body file://template.yaml"
5. You can attach role with above command as "--role-arn  arn:aws:iam::834697846023:role/cloudFormationRole"
