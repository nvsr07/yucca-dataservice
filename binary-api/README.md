insertdata-api
=================

This new project replaces data-insert project.

Main changes are:

* Switch from WSO2 technology (dataservice and ESB) to J2EE application for JBOSS
* Switch from MongoDB as speed layer to Phoenix (HBASE) and Solr as speed layer.


This service exposes http api for massive ingestion of data not realtime.


Main differences between this service and realtime http service are:

* Support for dataset bulk (not time series like social and stream datasets)
* Support more then one event (up to 1500 for dataset) 
* Support multiple ingestions at the same time for more then one dataset
* Does not send event for realtime streaming output (like web-socket or mqtt)
* Does not support MQTT protocol as input.

More informations are available here: http://developer.smartdatanet.it
 
