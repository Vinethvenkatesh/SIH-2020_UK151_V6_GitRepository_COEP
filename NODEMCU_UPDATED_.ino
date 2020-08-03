#include  <ESP8266WiFi.h>

#include <Firebase.h>
#include <FirebaseArduino.h>
#include <FirebaseCloudMessaging.h>
#include <FirebaseError.h>
#include <FirebaseHttpClient.h>
#include <FirebaseObject.h>

#include <SoftwareSerial.h>
SoftwareSerial uno(D6,D7);

#define r1 D5//arduino
#define r2 D4//case
#define r3 D3//ig


int RecVal;


#define WIFI_SSID "venkatesh" // Change the name of your WIFI
#define WIFI_PASSWORD "9840432071" // Change the password of your WIFI
#define FIREBASE_HOST "atsproject-a5b6d.firebaseio.com"
#define FIREBASE_AUTH "3YoZqvAUcFMGyluI17cpBOyF1ALdfJ5zRLSZFiPR"



void setup() {
   Serial.begin(9600);
   pinMode(r1, OUTPUT);
   digitalWrite(r1,LOW);
   pinMode(r2, OUTPUT);
   digitalWrite(r2,LOW);
    pinMode(r3, OUTPUT);
   digitalWrite(r3,HIGH);
   Serial.println("connecting.........");
   WiFi.begin (WIFI_SSID, WIFI_PASSWORD);
   while (WiFi.status() != WL_CONNECTED) {
    delay(500);
   // NodeMCU1.begin(4800);
   // NodeMCU2.begin(4800);
    uno.begin(4800);
   
  }
   Serial.println("connected");
  pinMode(2,OUTPUT);
  digitalWrite(2,LOW);
  Firebase.begin(FIREBASE_HOST);
  
}

void loop() {

  RecVal=Firebase.getString("command").toInt();
  
  
  Serial.println(RecVal);
  uno.write(RecVal);
  if(RecVal==1)
  {
    digitalWrite(r1,HIGH);
    digitalWrite(r3,LOW);
  }
  else if(RecVal==2)
  {
    digitalWrite(r1,LOW);
  delay(500); //Change the value with respect to the response or network speed
}
  else if(RecVal==3)
  {
    digitalWrite(r2,HIGH);
  }
  else if(RecVal==4)
  {
    digitalWrite(r2,LOW);
  delay(500); //Change the value with respect to the response or network speed
}
}
 
