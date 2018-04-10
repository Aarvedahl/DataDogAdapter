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

RMI
ISIM
TDI

##### Lista på teknologier

- Java SE

- IBM Tivoli Directory Integrator
its a way to transfer data from different systems. You have a lot of control over how to take in the data, modify it, and then output it. 
For instance, you can take in a csv file from a folder on a server, and then transfer that into a database via a web service.

- IBM Adapter Development Tool

- JavaScript


Lista på de bibliotek jag har använt:

- Apache Commons Logging
When writing a library it is very useful to log information. However there are many logging implementations out there, and a library cannot impose the use of a particular one on the overall application that the library is a part of.
Apache Commons Logging is a Java-based logging utility and a programming model for logging and for other toolkits. It provides APIs, log implementations, and wrapper implementations over some other tools

- Apache Http Components
The Apache HttpComponents™ project is responsible for creating and maintaining a toolset of low level Java components focused on HTTP and associated protocols.


- Google Gson
Gson är ett Java-bibliotek som kan användas för att konvertera Java-objekt till deras JSON-representation.
Det kan också användas för att konvertera en JSON-sträng till ett motsvarande Java-objekt.

- Log4j
Log4j is a fast and flexible framework for logging application debugging messages.
With log4j it is possible to enable logging at runtime without modifying the application binary. 
The log4j package is designed so that these statements can remain in shipped code without incurring a heavy performance cost. Logging behavior can be controlled by editing a configuration file, without touching the application binary. 





Java SE
För att bygga en adapter så krvädes det att antingen så skrev jag flera java-klasser som kompileras till en enda jar fil som sedan IBM Tivoli Directory Integrator skulle använda och anropa metoder. Det andra alternativet var att 
jag kunde skriva ren JavaScript direkt inuti IBM Adapter Development Tool. Då jag känner mig mest bekväm och det programmeringspråk som jag har vid hjärtat är Java så var detta ingen större fundering, utan jag visste direkt 
att jag ville skriva java-klasser till en jar fil. En annan sak som jag tycker underlättar vid själva kodningen är hur mycket enklare det är att lista ut fel, min erfarenhet är att det är lättare och tydligare att hitta just vad som är fel 
i Java om man skulle jämföra mot JavaScript.


IBM Tivoli Directory Integrator
Då detta är ett system som används för att överföra information mellan två system/filer antingen manuellt eller automatiskt, så är detta helt klart det lättaste sättet att ta informationen från användare i IBM Security Identity Manager och föra över till DataDog 
enligt min handledare. Jag har även gjort lite research och detta är det lättaste sättet, det andra sättet som skulle vara aktuellt är att använda WebService anrop direkt in i IBM Security Identity Manager. 
Eftersom vi inte modifierar informationen utav användaren i dagsläget utan bara skickar över den direkt som den är skriven, så använder jag långt ifrån alla funktioner i IBM Tivoli Directory Integrator. Tack vare detta system 
så har man bra möjligheter att utöka integrationen i framtiden. Detta är något är ett system som jag inte kände till innan så har jag lärt mig detta med hjälp av några andra som jobbar på företaget och att jag gjorde ett 
exempel på IBMs hemsida så gav detta mig goda grunder för att utveckla min adapter.


IBM Adapter Development Tool
Som man hör på namnet så är detta ett verktyg för att utveckla adaptrar, som är byggt på IBM Tivoli Directory Integrator. Här har man många av de möjligheter att modifiera information och objekt som finns i IBM Tivoli Directory Integrator. 
Det som detta främst används till är först att göra scheman för de attribut som behövs lagras i den aktuella databasen. Efter det så gör man forms där man specificeras vilka attribut som skall finnas med i de formulär som användaren 
fyller i när man skapar ett nytt konto som kopplas ihop med rätt attribut i respektive scheme. När det är gjort så mappar man vad som skall göras vid respektive operation, vilka attribut som skall ändras etc. 
Varför jag just valde detta, var för att det andra alternativet är att skapa subklasser till en superklass som IBM har byggt. Det är förstås något som man också kan göra, men eftersom mina arkitekt handledare hade en idé om 
den övergripande strukturen i IBM Adapter Development Tool samt att en av de nuvarande adaptrarna är byggda i detta verktyget. Det gjorde att jag såg absolut ingen anledning att välja det andra alternativet. 
Detta var inte det lättaste att lära sig, då det inte stöds utav IBM längre så det finns dåligt med dokumentation samt att arkitekterna visste inte heller hur det fungerade helt. Det tvingade mig att bryta loss en adapter som redan används och sedan 
försöka läsa mig till vad som händer. 


JavaScript
Javascript är det språk som används inuti IBM Tivoli Directory Integrator och IBM Adapter Development Tool, dock så kompileras den Javascripten till Java i runtime. Det finns faktiskt inga andra alternativ att skriva script i de systemen. 
Javascripten behövs då för att anropa rätt Java metod vid rätt operation. Jag har inte behövt att lära mig någon extra Javascript inför detta projektet.



Apache HttpComponents
Tanken var först att använda biblioteket OkHttp som är ett ganska välkänt bibliotek för att göra http request från Java klasser, samt att det används även i en av de nuvarande adapter.
Efter jag hade testat en del av mina metoder i IBM Tivoli Directory Integrator så visade det sig att den körde Java 1.6 vilket gjorde att man inte kunde köra jar filer som var kompilerade med något nyare Java.
Detta tvingande mig då att antingen skriva egna Http anrop eller att använda ett bibliotek, då jag visste att detta var ett ganska ambiöst projekt bestämde jag mig för att leta efter ett bibliotek. 
Efter jag hade läst igenom dokumentationen för Apache HttpComponents så tyckte jag att det verkade både hyfsat enkelt att använda och för att det var att utbudet utav http bibliotek som hade 
stöd för Java 1.6 var begränsad så bestämde jag mig för Apache HttpComponents. Sättet jag lärde mig Apache HttpComponents var genom att läsa en del utav deras dokumentation, gå igenom de exempel
som de hade på deras sida och när jag stötte på problem så sökte jag efter svar på Stack Overflow.

Google Gson
Eftersom jag skulle skicka JSON till DataDog's Rest-API så behövde jag något för att konverta mina Java Objekt till JSON objekt, detta brukar vanligtvis ingå i HTTP bibliotek men Apache HTTP Client hade inget stöd för detta.
Det var därför jag skulle leta efter bibliotek som kunde konverta Java Objekt till JSON men också för att tolka responsen från Rest-API:et så behövde jag ett bibliotek så jag kan konverta JSON till Java Objekt.
Det som gjorde att jag bestämde mig för Google Gson var främst enkelheten i det, att det fanns bra och tydlig dokumentation samt att det verkade vara ganska välanvänt vilket gjorde att om man skulle köra fast eller stöta på problem så 
fanns det många som hade haft liknande problem på Stack Overflow. Hur jag lärde mig Google Gson var helt enkelt genom att jag läste exemplen som fanns på deras Github, då jag endast behövde konvertera ett Java Objekt
 till JSON och tvärtom så gick det väldigt snabbt och smidigt att lära sig detta biblioteket.
 
Log4j
Standard biblioteket för logging i Apache Http Client, man kan givetvis byta ut detta mot önskat loggnings bibliotek. Men eftersom jag var bekant med detta och det är något som vi har kollat på i skolan så tyckte jag att det var 
ett bra alternativ. Ett par andra saker som är väldigt bra med Log4j är att det är enkelt att konfigurera eftersom det sker i en fil så det är smidigt att flytta konfiguration mellan maskiner. Samt att man har stor valmöjlighet på hur man 
vill logga, det finns sex stycken olika nivåer på loggningen och att man kan välja destinationen på loggningen. De alternativ som finns är UNIX konsolen, konsolen i den IDE, fil eller en databas. Detta är dock inget jag har exprimenterat med 
utan har hållt mig till konsolen i min IDE.


Apache Commons Logging
Detta är ett loggnings bibliotek som fungerar som en bro mellan Apache HttpComponents och olika loggnings implementationer. Ett bibliotek som använder commons loggings API kan användas vid vilken loggnings implementation vid runtime.
Det har support och stödjer en del populära loggnings implementationer. Detta kan bli avaktiverat vid behov men då det var aktiverat när jag implemeterade Apache HttpComponents, så såg jag ingen anledning att avaktiverade med tanke på den 
funktionaliteten som erbjöds. Commons Loggning ger dig detaljerad information (beroende på vilken lognivå du väljer) som jag har märkt underlättar enormt vid felsökning och debug. 
Jag kommer ihåg att det var ett antal gånger under utvecklingen av adaptern som saker inte alltid fungerade som de skulle. Tack vare detta bibilioteket så kunde jag snabbt se om isåfall vad som gick fel vid HTTP anropet, 
eller om det var något som jag gjorde före/efter HTTP anropet. 

 

#### lista på teknologier
kort beskrivning
motiv av val
OBS! om du lärt dig en ny teknologi så skriv även:
hur du lärde dig, resurser du använde
länka till repon på de mini-projekt du byggde för att lära dig


Kanske svårt att länka till tidigare mini projekt, men man kan ta en bild på hur det ser ut och sedan lägga in JS koden 


####Arkitektur
Då jag aldrig har byggt något liknande var detta en utmaning, det som var svårt var att man inte riktigt hade något att utgå ifrån.
Jag fick hjälp första veckan av arkitekten som ritade upp hur allt skulle hänga ihop, vilket verktyg som han tyckte skulle användas var. 

Arkitekturen på Javaklasser har jag haft eget ansvar för. Min tanke var att ha ett antal DTO objekt som ska mappas rätt vid varje JSON anrop oavsett om det är till eller från DataDog. Sedan så har jag en Adapter klass som sköter all "kommunikation" 
och tar hand om både att göra http anrop till DataDog men också att ta hand om det skulle bli fel.
För att hantera de operationer som inte DataDog hade stöd för så fick jag skapa ett Exception som jag namngav till MethodNotSupported. I dagsläget så var detta bara aktuellt för när man ville ta bort användare i IBM Security Identity Manager då 
det inte fanns något stöd på DataDogs API för just detta.  Jag har visserligen inte använt mig utav några design patterns vad jag känner till, antagligen för detta var ett annorlunda program och inget som jag var van vid. 
Utan idén och koncept som jag ville skulle genomsyra projektet var att skriva programmet i små metoder som är lätta att läsa samt att det skall vara simpelt att bygga ut vid behov.

Attrubuten på de DTO klasserna bestämdes genom att undersöka och se hur de JSON objekten är strukturerade för att matcha och mappa responsen på HTTP anropen. För att detta skulle bli korrekt och för att Google Gson mappa rätt värde med det attribut 
så krävdes både samma namn på attributen och på klasserna. 
Sättet jag har valt att strukturera metoderna i min adapter klass, var helt enkelt att jag hade en metod för varje operation som skulle göras till API:et. 
För att återanvända så mycket utav samma kod som möjligt så har jag en metod som heter makeRequest som tar emot en HTTP request samt ett ResultDTO objekt som i sin tur returnerar 
alla värden som http anropet returnernar. Detta sker med hjälp av ResponseDTO attributet i ResultDTO, ResponseDTO håller i sin tur sedan alla värden från själva objektet i anropet.
Det första som makeRequest gör att skicka anropet och sedan anropar den metoden handleStatusCode som hanterar status koden på anropet. Om status koden skulle vara 408 eller 409 så försöker vi ett par gånger till för att se om det lyckas 
och om det inte finns en korrekt status kod så kastar vi ett exception och anropet misslyckas där med. 

HandleStatusCode använder också en annan speciell metod, nämnligen getSSL. Detta var en metod som krävdes för att göra anrop ifrån IBM Tivoli Directory Integrator. 
Det som denna metoden gör är att validera SSL certifikatet, genom att se så att det SSL certifikatet på DataDog stämmer överens med det som finns i truststore. Då jag har satt det till null 
betyder det att alla SSL certifikat kommer att valideras, och detta är inget att rekommendera om man ska använda i produktions miljöer då det finns risk att informationen i http anropet 
går igenom en tredje part.


Nu i efterhand, så skulle jag antagligen strukturerat det på ett annorlunda sätt. Eftersom min adapter klass sköter all kommunikation och tar emot anrop så om jag skulle detta projektet igen så hade jag istället gjort så att det blir en klass 
som bara har hand om att ta emot anropen och sedan har jag en klass som tar hand om att skicka http anrop till DataDog. Vinsten i detta skulle varit att det blir både lättare att läsa koden och gå igenom vad det är som händer vid de olika anropen 
och att det blir lättare att bygga ut den vid behov.



#### Arkitektur 
klassdiagram
motiv
design patterns andra koncept/idéer

#### Tidrapport

##### Ursprunglig Plan
Hur har jag tänkt att gå tillväga med adaptern.
Första veckan har jag planerat att läsa på om hur TDI fungerar samt Datadog's API. Detta kommer ge mig inblick hur jag ska göra ett upplägg över projektet. 
Jag har även tänkt att läsa på och förstå den nuvarande SCIM adaptern för att få en förståelse på hur min adapter kommer se ut.

Andra veckan är planen bekanta mig med TDI och göra något exempel. 
Efter det är planen att fortsätta med adaptern och börja att göra/ta emot API anrop från adaptern. Detta skall ske med hjälp utav tester i Java.

Tredje veckan är att testa om jag kan komma åt de javaklasser som jag själv har skrivit. Antagligen kommer det inte ske felfritt utan kommer behöva lägga en del tid på detta.
När jag väl kommer åt de javaklasser inifrån en script-nod som jag skriver själv kan jag börja med enklare TDI jobb. T.ex att jag läser in från en CSV-fil och sedan lägger till de i Datadog.

Fjärde veckan är tanken att när väl det fungerar bra med kopplingen mellan javaklasser och TDI. Så ska jag testa att koppla ISIM och TDI, samt fortsätta färdigställa javaklasserna.

Femte veckan ska jag färdigställa kopplingen mellan ISIM och TDI samt javaklasserna.

Och de sista två veckor ska spenderas till att skriva rapporten.


##### Senast Reviderad
Hur har jag tänkt att gå tillväga med adaptern.
Första veckan har jag planerat att läsa på om hur TDI fungerar samt Datadog's API. Detta kommer ge mig inblick hur jag ska göra ett upplägg över projektet. 
Jag har även tänkt att läsa på och förstå den nuvarande SCIM adaptern för att få en förståelse på hur min adapter kommer se ut. Gör även ett exempel i TDI.

Andra veckan är planen att skriva det mesta på java klasserna, göra tester för att se till så metoderna gör anrop till DataDog 

Tredje veckan är planen att avsluta och bli färdig med java klasserna, bygg om så java klasserna och biblioteken har stöd för Java 1.6.
I slutet av veckan är tanken att läsa in information från en CSV fil och sidan anropa metoderna så t.ex användare läggs till i DataDog

Fjärde veckan är planen att läsa på om ADT och hur man skapar en adapter profil

Femte veckan är planen att skapa en adapter profil så att det finns rätt värden och attribut i den och börja skriva lite JavaScript.

Sjätte veckan skall adapter profilen avslutas och javascripten ska bli färdig. Undersökning hur man kan importera den in i IBM Security Identity Manager.
Börja och skriv de 2-3 första delarna i uppsatsen.

Sjunde veckan skall adapter profilen importeras och uppsatsen ska avslutas.



##### Reflektion

Reflektion varför det blev så stora förändringar i tidsplanen

Anledningen till varför jag ändrade planen, den första förändringen var att jag gjorde i ett exempel i TDI första veckan istället för andra. Det var väl främst för att jag märkte att det jag fick en bra förståelse för hur TDI fungerade snabbare 
än vad jag hade tänkt mig. Och då var det lika bra att göra ett exempel för att fördjupa min förståelse. Den ursprungliga planen var att göra mer testning med mina java klasser och TDI innan jag färdigställde dem. 
Sen så märkte jag att det var mycket kod som kunde användas flera gånger, vilket gjorde att det gick mycket snabbare och enklare att få färdig java klasserna. 

Det blev ganska stora förändringar i tidsplanen gällande tredje veckan. Tanken var att koppla från TDI och lägga till de i DataDog. Det blev väl så man det var mycket krångel att få det att fungera. Saken var den att måndagen tänkte jag 
börja skriva script direkt in i TDI för att anropa mina Java klasser. Tyvärr blev det inte så enkelt, utan jag fick java.lang.UnsupportedClassVersionError vilket gjorde att jag fick bygga om en större del utav java klasser så att de hade stöd för java 1.6, 
det som var jobbigast var att behöva hitta nya bibliotek som hade stöd för java 1.6 och fick det att fungera. Men det fungerade på fredagen av den tredje veckan.

Det fjärde veckan blev ungefär som jag hade tänkt mig, att påbörja kopplingen mellan ISIM och TDI. När jag påbörjade projektet visste jag inte hur detta skulle gå till. Så den fjärde veckan blev mest att läsa om hur man skapar en Adapter profil så 
att ISIM har stöd för adaptern och känner igen värderna samt vet vad som ska hända vid varje operation. Femte veckan blev inte alls som jag hade tänkt mig, som du kan se i tidsplanen så jag hade tänkt mig att färdigställa installationen utav adaptern. 
Tyvärr så hade jag varit för optimisktiskt angående installationen, så under den femte veckan så hann jag bara ett börja skriva in adapter profilen. Under sjätte och sjunde veckan blev även det förändringar, orsaken till varför är för att jag inte är riktigt 
färdig med installation. Javascripten blev färdig ganska snabbt, så det som är svårast är själva installation utav adaptern och importen utav  adapter profilen. Rapporten har givetvis prioritet, och när jag har tid över så fortsätter jag att installera adaptern.

För att sammanfatta detta kort, så skulle jag säga att jag var lite pessimisitks gällande att skriva java klasserna och trodde att det skulle ta längre tid än vad det egentligen gjorde. Medans jag var optimisktiskt och trodde att det andra skulle gå mycket snabbare 
än vad det har gjort. I tidigare projekt har jag haft samma tendens, jag har alltid varit pessimistiskt gällande min kodning och tror alltid att det tar längre tid än vad det har gjort, och samma optimism när det har varit rörande andra saker. Så det är nog tid att 
ändra på detta,

####tidrapport
   ursprungliga planen
   senaste reviderade
   reflektion kring varför du ändrade planen





##### Loggbok



####resultat (retrospect)
   vad tar du med dig från projetet? vad hade du gjort annorlunda om du skulle göra om samma projekt?
    produkten
    tidsplan / projektets utförande
    teknologier
    arkitektur / design patterns (vidareutveckla)
    "hade jag haft mer tid hade jag….”

