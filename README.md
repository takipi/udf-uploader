# UDF Uploader

Simple utility to upload user defined functions

## Build

Build the UDF Uploader jar file with Gradle:

```console
./gradlew fatJar
```

## Usage

From the `libs` directory, run the uploader on the command line:

```console
cd udf-uploader/build/libs
```

```console
Usage: java -jar udf-uploader-1.0.0.jar [-h hostname] [-k api key] [-s service id|-g] [-f filename]
```

For example:

```console
java -jar udf-uploader-1.0.0.jar \
-h https://api.overops.com \
-k <YOUR API KEY HERE> \
-s Sxxxxx \
-f path/to/UDF.jar
```

### Notes

* For SaaS installations, hostname is `https://api.overops.com`. For on prem, use the hostname of your OverOps backend server, including protocol and port.
* Your API Key can be found on the Account Settings screen in OverOps.
* Service ID or Environment ID begins with "S" followed by numbers. Specify either the service ID `-s` flag, or `-g` flag to upload the UDF globally.

For more information, see the OverOps API documentation for [adding a new UDF library](https://doc.overops.com/reference#post_services-env-id-udfs) and [adding a new global UDF library](https://doc.overops.com/reference#post_global-settings-udfs).

OverOps maintains an [open source UDF library](https://github.com/takipi/overops-functions/) which is automatically enabled for all users. For more details on writing your own, see [User Defined Functions](https://github.com/takipi-field/udf).