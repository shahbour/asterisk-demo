# asterisk-demo

To run this code type
1- ./mvnw springboot:run
2- curl http://localhost:8080?from=96130xxxx&to=9613050252

This shall send a call to endpoint /SIP/xxx/to with extension 1002 at context default 

exten => 1002,1,NoOp()
 same =>      n,Stasis(${originateStasis},${callId})
 same =>      n,,Hangup()
 
The call should return back to asterisk to simulate that we recieved a call into this extension 

exten => 9613050252,1,NoOp()
; same =>      n,Answer()
 same =>      n,Stasis(test-dump)
 same =>      n,Hangup()

Now after the call is done we see the memory leak
