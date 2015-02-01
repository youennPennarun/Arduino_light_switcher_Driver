
int led = 13;
int relay = 3;
char recv = ' '; // byte recu sur le serial port
char lightId = 'l';
char valueHight = 'h';
char valueLow = 'l';
char id = ' ';
  
void setup()
{
  pinMode(relay, OUTPUT);   
  pinMode(led, OUTPUT);     
  digitalWrite(led, HIGH);  // on alimente la LED
  Serial.begin(9600); // serial de communication avec l'arduino
}
void loop()
{
         
  if (Serial.available() > 0) { // Si le port est disponible, lire ce qui arrive
      recv = Serial.read();
      Serial.print("--Arduino received: ");
      Serial.println(recv);
      if(id == ' '){ 
        if(recv == lightId){ // Si la valeur trouvé est une led on utilise cette led
          Serial.println("waiting for led value");
          id = recv;
        }
      }else{
        if(id == lightId){  // on a recu l'id de la LED => on regarde quel est la valeur associé
            if (recv == valueHight){  // on a recu un 'h' du programme Java => on allume le relais
              Serial.println("led 1 on");
              digitalWrite(relay, HIGH);
            } else if (recv == valueLow){ // on a recu un 'l' du programme Java => on eteind le relais
              Serial.println("led 1 off");
              digitalWrite(relay, LOW);
            } else {
              Serial.print("invalid value ");
            Serial.println(valueHight);
            }
        }
        id = ' ';
    }
  }
}

