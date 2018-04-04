# Adapter till IBM Security Identity Manager
#### Adapter mellan IBM Security Identity Manager och Datadog
Sammanfattning?



Idag så använder Husqvarna ett användargränsnitt (IBM Security Identity Manager) för att 
göra deras hantering utav användare för olika plattformar/målsystem. Och för att de ville 
och kände behovet utav att utöka detta och hantera användare för ett nytt målsystem. 
Till detta behövdes då en adapter för att kunna hantera användare på det nya målsystemet DataDog.


Målet med adaptern är att den skall kunna ha möjlighet att hantera användare och göra de 
följande operationerna:
- Skapa en användare
- Uppdatera en användare
- Ta bort en användare
- Visa information om en användare


Global service desk hantera konton åt användare (Återförsäljare och andra anställa på Husqvarna)


Själva syftet med produkten är att låta främst supporttekniker hantera konton på målsystemet 
åt användare i IBM Security Identity Manager.  Eftersom just detta målsystemet DataDog inte 
hade stöd för någon av de existerande adapterna så var jag tvungen att bygga en egen adapter
som kan ta emot informationen av en använadre från IBM Security Identity Manager och skicka
de uppgifterna i rätt format till DataDog, detta sker med hjälp utav JSON Rest-API.


En förändring som jag behövde göra, efter att ha kollat och läst igenom dokumentationen
för DataDogs API så såg jag att API:et inte hade stöd för att ta bort användare. Efter
att jag hade diskuterat med min handledare om vad som vore bäst så kom vi fram till att
det bästa vore att kasta ett exception som säger att metoden inte stöds.

En annan förändring som jag gjorde med tanke på att API:et inte hade stöd för att ta bort 
användare, var att eftersom jag såg att API:et hade stöd för att inaktivera och aktivera
användare så byggde byggde jag stöd för dessa operationer.


#### Inledning utförlig beskrivning av produkten
idé - Förklaring till användandet, varför?

mål

syfte

“vad är detta?”

historik över förändringar (motiv, förklaring)

#### Bakgrund och förklaring av begrepp



##### Lista på teknologier

- Java SE
- IBM Tivoli Directory Integrator
- IBM Adapter Development Tool
- JavaScript

Lista på de bibliotek jag har använt:

- Apache Commons Logging
- Google Gson
Gson is a Java library that can be used to convert Java Objects into their JSON representation. 
It can also be used to convert a JSON string to an equivalent Java object. 
- Apache HttpClient
- Jackson Core
- Log4j


Apache Http Client
Tanken var först att använda biblioteket OkHttp som är ett ganska välkänd bibliotek för att göra http request från Java klasser, samt att det används även i en av de nuvarande adapter.
Efter jag hade testat en del av mina metoder i IBM Tivoli Directory Integrator så visade det sig att den körde Java 1.6 vilket gjorde att man inte kunde köra jar filer som var kompilerade med något nyare Java.
Detta tvingande mig då att antingen skriva egna Http anrop eller att använda ett bibliotek, då jag visste att detta var ett ganska ambiöst projekt bestämde jag mig för att leta efter ett bibliotek. 
Efter jag hade läst igenom dokumentationen för Apache Http Client så tyckte jag att det verkade både hyfsat enkelt att använda och för att det var att utbudet utav http bibliotek som hade 
stöd för Java 1.6 var begränsad så bestämde jag mig för Apache Http Client. Sättet jag lärde mig Apache Http Client var genom att läsa en del utav deras dokumentation, gå igenom de exempel
som de hade på deras sida och när jag stötte på problem så sökte jag efter svar på Stack Overflow.


Google Gson
Eftersom jag skulle skicka JSON till DataDog's Rest-API så behövde jag något för att konverta mina Java Objekt till JSON objekt, detta brukar vanligtvis ingå i HTTP bibliotek men Apache HTTP Client hade inget stöd för detta.
Det var därför jag skulle leta efter bibliotek som kunde konverta Java Objekt till JSON men också för att tolka responsen från Rest-API:et så behövde jag ett bibliotek så jag kan konverta JSON till Java Objekt.
Det som gjorde att jag bestämde mig för Google Gson var främst enkelheten i det, att det fanns bra och tydlig dokumentation samt att det verkade vara ganska välanvänt vilket gjorde att om man skulle köra fast eller stöta på problem så 
fanns det många som hade haft liknande problem på Stack Overflow. Hur jag lärde mig Google Gson var helt enkelt genom att jag läste exemplen som fanns på deras Github, då jag endast behövde konvertera ett Java Objekt
 till JSON och tvärtom så gick det väldigt snabbt och smidigt att lära sig detta biblioteket.
 
 

#### lista på teknologier
kort beskrivning
motiv av val
OBS! om du lärt dig en ny teknologi så skriv även:
hur du lärde dig, resurser du använde
länka till repon på de mini-projekt du byggde för att lära dig

