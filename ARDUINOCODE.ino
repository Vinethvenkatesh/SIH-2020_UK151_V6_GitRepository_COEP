#include <TinyGPS.h>
TinyGPS gps; 

float lat = 12.9647, lon = 80.1961;  

const int fuel_level_pin = A0;
int fuel_level;

String latitude;
String longitude;

const int TrigPin = 3;
const int EchoPin = 2;
long period, interval;

void message1(void);
void message2(void);
void message3(void);

String readstring;

#define AlarmPin 4

void setup()
{
  pinMode(AlarmPin, OUTPUT);
  digitalWrite(AlarmPin,LOW);
  pinMode(TrigPin, OUTPUT);
  pinMode(EchoPin, INPUT);
 
  Serial.begin(9600);// baud rate

  Serial1.begin(9600); //  gsm 
 
  Serial2.begin(9600); //  gps 
}

void loop()
{  
  latitude = String(lat,6);
  longitude = String(lon,6);

  //Ultrasonic 
  digitalWrite(TrigPin,LOW);
  delayMicroseconds(2);
  digitalWrite(TrigPin,HIGH);
  delayMicroseconds(10);
  digitalWrite(TrigPin,LOW);
  period = pulseIn(EchoPin,HIGH);
  interval = period/58.2;
  Serial.print("Distance: ");
  Serial.print(interval);  
  Serial.println(" cm");
 
  if(interval >= 20 || interval==0)//ground clearance should be considered
  {
    message1();
    digitalWrite(AlarmPin,HIGH);
    Serial.println();
    delay(500);    
  }
 
  //Fuel Level SENSOR
  fuel_level = analogRead(fuel_level_pin);

  fuel_level = map(fuel_level, 0, 1023, 0 , 100);
 
  Serial.print("Fuel Level Percentage: ");
  Serial.println(fuel_level);
 
  if(fuel_level <= fuel_level-2 || fuel_level>= fuel_level+2)
  {
    message2();
    digitalWrite(AlarmPin,HIGH);
    Serial.println();
    delay(500);
  }

  //GPS
  while (Serial.available())
  {
    delay(10);
    char c = Serial.read();
    readstring += c;
  }  
 
  if (readstring.length() > 0)
  {
    Serial.println(readstring);
   
    if(1)// add the server call inside if case
    {
      latitude = String(lat,6);
      longitude = String(lon,6);

      if((lat <= lat - 0.0100) || (lat >= lat + 0.0100) && (lon <= lon - 0.0100) || (lon >= lon + 0.0100))
      {
        message3();
        digitalWrite(AlarmPin,HIGH);
        Serial.println();
        delay(500);  
      }  
    }
   
    readstring="";   //Reset the variable
  }
  //
 
  while(Serial2.available())
  { 
    if(gps.encode(Serial2.read()))
    {  
      gps.f_get_position(&lat,&lon); 
      Serial.print("Position: ");
      Serial.print("Latitude:");
      Serial.print(lat,6);
      Serial.print(";");
      Serial.print("Longitude:");
      Serial.println(lon,6);
     
      latitude = String(lat,6);
      longitude = String(lon,6);
 
      Serial.println(latitude+";"+longitude);
      delay(500);
    }
  }

  Serial.println("******************************************************");
  Serial.println("         ");
  delay(1000);
}

void message1(void)
{
  Serial1.print("AT\r\n");delay(800);
  Serial1.print("AT+CMGF=1\r\n");delay(800);
  Serial1.print("AT+CMGS=");delay(500);
  Serial1.write('"');delay(500);
  Serial1.print("7092483899");delay(500);
  Serial1.write('"');
  Serial1.print("\r\n");delay(500);
  Serial1.print("'Theft drtected. Secure vehicle'");delay(500);//17
  delay(500);
  Serial1.write(26);
}

void message2(void)
{
  Serial1.print("AT\r\n");delay(800);
  Serial1.print("AT+CMGF=1\r\n");delay(800);
  Serial1.print("AT+CMGS=");delay(500);
  Serial1.write('"');delay(500);
  Serial1.print("7092483899");delay(500);
  Serial1.write('"');
  Serial1.print("\r\n");delay(500);
  Serial1.print("'Theft drtected. Secure vehicle'\r\n lat:");delay(500);//17
  delay(500);
  Serial1.write(26);
}

void message3(void)
{
  Serial1.print("AT\r\n");delay(800);
  Serial1.print("AT+CMGF=1\r\n");delay(800);
  Serial1.print("AT+CMGS=");delay(500);
  Serial1.write('"');delay(500);
  Serial1.print("7092483899");delay(500);
  Serial1.write('"');
  Serial1.print("\r\n");delay(500);
  Serial1.print("'GPS Location Different'\r\n lat:"+latitude+";long:"+longitude);delay(500);//17
  delay(500);
  Serial1.write(26);
}
