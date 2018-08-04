### MqttListener

This project aim to create a web-based interface to receive and record JSON formatted messages on certain Mqtt topics. 
It is essentially an experimenting servlet provide basic functionailty of a simple Mqtt client. The project is implemented
base on java paho mqtt client library.


### Operation
Currently hosting at [an example](http://www.verbose.me). Only tested with local Mqtt Broker at default port(1883), but should work with
external broker if setup correctly with Url, Username and Password. The Java Container used for hosting is tomcat v8.5, so might require
minor modification for other java container.

### Usage
This application can be used as a tool to moniter activities on select topic. However since the implementation does not create 
seperate session for each user, the change of the states(connect to new broker, subscribe to new topic) is server-wide.
For personal use, one should host the servlet on their own instance.

### More
This project will be continuly updating
