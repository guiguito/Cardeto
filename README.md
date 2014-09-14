Cardeto
============
## Convenient Android Remote DEbugging TOol

![Logo](logo/logo.png)

*Cardeto* is a simple android library which help you debug your app in an original way.

Starting cardeto service within your app, you'll be able to get information from your app at runtime without using ADB and directly from your desktop web browser.
Cardeto currently proposes several small modules :
- DB browser : browse you SQLITE databases very easily. Query it directly from you web browser.
- logcat visualizer : watch logcat from your web browser
- visualize static variables : watch all static variables of a class.
- copy/paste : get value from clipboard and put text in it.
- app info : watch app info at runtime.

## How does it work ?
Cardeto is very simple to use and modify. It runs an http server within your app process.
The embedded http server is based on nanoHttpd (https://github.com/NanoHttpd/nanohttpd).
Cardeto handle you browser request and generates an HTML output.
The code architecture is primitive but efficient and easy to modify.

## How to use Cardeto ?

Your smartphone must be on the wifi network from which you'll reach via a web browser.
In your manifest add the following permissions and service

```java
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.READ_LOGS" />

    <application>
       <service android:name="com.ggt.cardeto.CardetoService" />
    </application>
```


```java
    //start cardeto service
    Intent intent = new Intent(this, CardetoService.class);
    intent.putExtra(CardetoService.CARDETO_PORT, 2000);
    startService(intent);

    //start cardeto service
    stopService(new Intent(this, CardetoService.class));
```

Warning : Do not publish your app with Cardeto in it !
It is only supposed to be used during development phase.

## Support
This library was made to help me in the making of a professional app (big sqlite DB to debug).
I built it more than a year ago but never took the time to release it as open source.
I don't  plan to provide any support neither to update it. But who knows ...
I just figured that it was an original way to debug your DB and that it might be useful to someone.
Do not hesitate to modify it or update it.

License
-------

    Copyright 2014 Guilhem Duché

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.



 [1]: https://github.com/guiguito
 [2]: https://github.com/NanoHttpd/nanohttpd