#include <PID_v1.h>
#include <ESP8266WiFi.h>
#include <ESPAsyncTCP.h>
#include <ESPAsyncWebSrv.h>
#include <dht11.h>
double Setpoint, Input, Output;
PID pid(&Input, &Output, &Setpoint,2,5,1, DIRECT);

const char *ssid = "ESP8266-Access-Point";
const char *password = "123456789";
#define DHTPIN 4 // Digital pin connected to the DHT sensor
#define RED "red"
#define GREEN "green"
#define BLUE "blue"
#define YELLOW "yellow"
#define PIDout 16
#define PIDTEMP "temp"
#define GPIO_RED 2
#define GPIO_BLUE 16
#define GPIO_GREEN 12
#define GPIO_YELLOW 0
#define ACTEMP PIDTEMP


// current temperature & humidity, updated in loop()
int t = 0;
int h = 0;

int CRT_R, CRT_G, CRT_B;

int timeGreen=0, timeRed=0, timeYellow=0;//seconds

int acTemp=0;

int timePerColor=0;
int color=4;

// Create AsyncWebServer object on port 80
AsyncWebServer server(80);

#define DHT11PIN 4
dht11 DHT11;

void setup()
{
  sei();
  // Serial port for debugging purposes
  Serial.begin(115200);
  Input = 0;
  Setpoint = 25;
  //turn the PID on
  pid.SetMode(AUTOMATIC);
  Serial.print("Setting AP (Access Point)…");
  // Remove the password parameter, if you want the AP (Access Point) to be open
  WiFi.softAP(ssid, password);
  IPAddress IP = WiFi.softAPIP();
  Serial.print("AP IP address: ");
  Serial.println(IP);

  // Print ESP8266 Local IP Address
  Serial.println(WiFi.localIP());

  server.on("/semafor", HTTP_POST, [](AsyncWebServerRequest *request)
            {
        String message="";
        if (request->hasParam(RED) || request->hasParam(GREEN) || request->hasParam(YELLOW)) {
            message += request->getParam(RED)->value();
            message += request->getParam(GREEN)->value();
            message += request->getParam(YELLOW)->value();
            timeRed = request->getParam(RED)->value().toInt();
            timeGreen = request->getParam(GREEN)->value().toInt();
            timeYellow = request->getParam(YELLOW)->value().toInt();
            Serial.println("Params: "+message);
            analogWrite(GPIO_RED,0);
  analogWrite(GPIO_GREEN,0);
  analogWrite(GPIO_YELLOW,0);
            color=0;
        } else {
            message = "No message sent";
            message+=" " + request->pathArg(0);
            Serial.println("Params: "+message);
        }
        request->send(200, "text/plain", "Hello, GET: " + message); });

  server.on("/temperature", HTTP_GET, [](AsyncWebServerRequest *request)
            {
              String str=String(t);
              //itoa(t,str,3);
              Serial.print("Temperature (C): ");
              Serial.println(t);
              request->send(200, "text/plain", str); });

  
  server.on("/humidity", HTTP_GET, [](AsyncWebServerRequest *request)
            { 
              String str=String(h);
              //itoa(h,str,3);
              Serial.print("Humidity (%): ");
              Serial.println(h);
              request->send(200, "text/plain", str);
              });


  server.on("/RGB", HTTP_POST, [](AsyncWebServerRequest *request)
            {
        String message="";
        if (request->hasParam(RED) || request->hasParam(GREEN) || request->hasParam(BLUE)) {
            message += request->getParam(RED)->value();
            message += request->getParam(GREEN)->value();
            message += request->getParam(BLUE)->value();
            CRT_R = request->getParam(RED)->value().toInt();
            CRT_G = request->getParam(GREEN)->value().toInt();
            CRT_B = request->getParam(BLUE)->value().toInt();
            setColor(CRT_R,CRT_G,CRT_B);
        } else {
            message = "No message sent";
            message+=" " + request->pathArg(0);
        }
        request->send(200, "text/plain", "POST " + message); });


  server.on("/pid", HTTP_POST, [](AsyncWebServerRequest *request)
            {
        String message="";
        if (request->hasParam(PIDTEMP)) {
            message += request->getParam(PIDTEMP)->value();
            Setpoint = (double)request->getParam(PIDTEMP)->value().toInt();
            Serial.println("Params PID: "+message);
            pidCalc();
        } else {
            message = "No message sent";
            message+=" " + request->pathArg(0);
            Serial.println("Params: "+message);
        }
        request->send(200, "text/plain", message); });

  server.on("/AC", HTTP_POST, [](AsyncWebServerRequest *request)
            {
        String message="";
        if (request->hasParam(ACTEMP)||request->hasParam(ACPOWER)) {
            message += request->getParam(ACTEMP)->value();
            acTemp = request->getParam(ACTEMP)->value().toInt();
            acPower=request->getParam(ACPOWER)->value().toInt();
            setAc();
        } else {
            message = "No message sent";
            message+=" " + request->pathArg(0);
            Serial.println("Params: "+message);
        }
        request->send(200, "text/plain", message); });

  // Start server
  server.begin();
}


void pidCalc(){
  pid.Compute();
  analogWrite(PIDout,(int)Output);
}


void setSemafor(int red, int green,int yellow){
  if(timePerColor==green && color==0)
  {
    color=1;
    timePerColor=0;
  }
  else{
    if(timePerColor==yellow && color==1)
    {
      color=2;
      timePerColor=0;
    }
    else
    if(timePerColor==red && color==2)
    {color=0;
timePerColor=0;
    }
  }
  delay(1000);
  timePerColor++;
}

void setColor(int red,int green,int blue){
  analogWrite(GPIO_RED,red);
  analogWrite(GPIO_GREEN,green);
  analogWrite(GPIO_YELLOW,blue);
}

void turnSemaforOn(){
  switch(color){
    case 0: analogWrite(GPIO_GREEN,255);
analogWrite(GPIO_YELLOW,0);
analogWrite(GPIO_RED,0);
    break;
    case 1: analogWrite(GPIO_YELLOW,255);
    analogWrite(GPIO_GREEN,0);
    analogWrite(GPIO_RED,0);
    break;
    case 2: analogWrite(GPIO_RED,255);
    analogWrite(GPIO_YELLOW,0);
    analogWrite(GPIO_GREEN,0);
    break;
    default: 
    break;
  }
}

void loop()
{

DHT11.read(DHT11PIN);
t=(float)DHT11.temperature;
h=(float)DHT11.humidity;
pidCalc();
setSemafor(timeRed,timeGreen,timeYellow);
turnSemaforOn();
}
