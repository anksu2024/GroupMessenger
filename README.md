GroupMessenger
==============

Group Chatting Applications to simulate group communication among 5 Android Devices. Something similar to what Whatsapp does

PROJECT SPECIFICATION: https://docs.google.com/document/d/1KxjccTa7bza9eQWsNMCqkZIkGR7QbUHQYv_LlQUYZMA/pub

TEST SCRIPTS: The test scripts (Depending on the Operating System) can be downloaded from the links provided in the project specifications.

NOTE: FYR the project specifications and test scripts are also provided in the Respective Folders

11 STEPS TO REALIZE THIS PROJECT:

1) Check the directory "Test Scripts for Project".

2) This directory contains the python scripts that are to be executed to test the project

3) Under this directory in the Terminal execute the script 'create_avd.py' to create 5 AVDs Use the command "python create_avd.py 5"

4) Once the AVDs are created, excute the script 'run_avd.py' script to start 5 AVDs For the purpose use the command: "python run_avd.py 5"

5) When the 2 AVDs are up and running, each AVD needs to be assigned a port number

6) To do this execute the script 'set_redir.py' Command to be used: "python set_redir.py 10000" This command will make sure that all the AVDs are connected to each other by a single Server port number 10000

7) Now the Tester is good to go ahead with the execution of the App on the AVDs

8) For this project Eclipse was used to install the GroupMessenger App on the AVDs

9) Once the App is up and running on both the AVDs, the Tester may play around with it

10) Type some text in Edit Text given in the bottom of the App in the AVD

11) Press Enter Key and all other AVDs receive the text. Same goes with the other AVD

TO DO AUTOMATED TESTING (Mac OS):

1) Use the script "groupmessenger-grading.osx" script given in the Test Script folder to perform the automated testing of the code.

2) Make sure that "775" permissions are provided to the script. To do that use : chmod 755 groupmessenger-grading.osx

3) Install the App on the AVDs.

4) Execute the script from its directory using : ./groupmessenger-grading.osx

5) Watch the MAGIC happen... ;)

To send in comments or report a bug: sarrafan[at]buffalo[dot]edu
