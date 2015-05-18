#!/bin/bash

echo "Application starting.."
java -cp target/NodesCommunication-0.0.1-SNAPSHOT.jar com.anand.nodes.test.NodeCommunication 127.0.0.1 8080

echo "Application processing completed !"