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




#### lista på teknologier
kort beskrivning
motiv av val
OBS! om du lärt dig en ny teknologi så skriv även:
hur du lärde dig, resurser du använde
länka till repon på de mini-projekt du byggde för att lära dig

