# AndroidTwilioSMS

SMS messages from android using Twilio API

Folder Structure:

    AndroidTwilioSMS
    
          - DownTimeAlerter
          
          - Server_DTA(DTA means DownTimeAlerter)
      
DownTimeAlerter - Import this folder in Android Studio

Server_DTA - Import this folder in InteliJ IDEA Community

Workflow of Application:

    1) At first we need to run our server(Server_DTA) in local machine.
    2) Then create a public url using ngrok and use this url in android.
    3) Run DownTimeAlerter in your mobile using Android Studio.
    
After installing the app, we need to give any url as input. In background application checks the response code of the url. If the response code other than 200 means it send a text sms using Twilio API.
