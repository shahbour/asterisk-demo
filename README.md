# asterisk-demo

To run this code type

* ./mvnw springboot:run
* curl http://localhost:8080?from=96130xxxx&to=9613050252

This shall send a call to endpoint /SIP/xxx/to with extension 1002 at context default 

```
exten => 1002,1,NoOp()
 same =>      n,Stasis(${originateStasis},${callId})
 same =>      n,,Hangup()
```
The call should return back to asterisk to simulate that we recieved a call into this extension 
```
exten => 9613050252,1,NoOp()
; same =>      n,Answer()
 same =>      n,Stasis(test-dump)
 same =>      n,Hangup()
```
Now after the call is done we see the memory leak

* default settings
```
asterisk:
  url: http://192.168.70.80:8088/
  username: risk
  password: risk
  originate-stasis: received-test
  terminate-stasis: test-dump
  end-point: SonusUK
  call-delay: 10
logging:
  level:
    com.shahbour: debug
```

* Settings can be changed from application.yml in resources folder
