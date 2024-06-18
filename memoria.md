Proyecto integrado
==================

## Sobre este proyecto
### Control de versiones
El control de versiones que se usará durante el desarrollo de este proyecto es git.

### Licencia de uso
Salvo que se especifique lo contrario en algún archivo concreto, el proyecto se encuentra bajo la
licencia GPLv3. Para más información ver [LICENSE](/LICENSE).

## Análisis del problema
### Introducción al problema
Existen innumerables de aplicaciones que permiten reproducir archivos de audio en Android, pero pocos
de ellos integran alguna funcionalidad para reproducir canciones mediante streaming dada una URL
remota. 

### Antecedentes
[mpv](https://mpv.io/) es un reproductor multimedia que permite reproducir media en remoto y se encuentra 
disponible para Android pero mediante builds no oficiales y con la interfaz por defecto del escritorio.   

Otra aplicación que sí permite reproducir canciones y vídeos es [NewPipe](https://newpipe.net/), además
de crear listas de reproducción y tener una funcionalidad similar a la aplicación oficial de YouTube
para Android. Su mayor ventaja es que no hace uso de la API oficial de Google o Soundcloud para acceder
a estos servicios y en su lugar hace scrapping a las páginas web mediante su propia librería, librándose
de cualquier problema legal que se derive de usar estas APIs. No obstante, actualmente el desarrollo
se encuentra algo parado debido a que se están reescribiendo gran parte de su codebase.   

En cuanto a las aplicaciones de Android enfocadas exclusivamente en reproducir canciones localmente,
casi todas ellas hacen uso de la API de Android mediante Mediastore para obtener información sobre la
librería de canciones del usuario. Sin embargo, esta base de datos de Android no se actualiza de manera
inmediata y además es un poco lenta.   

### Objetivos
Dar a los usuarios la posibilidad de usar una aplicación fácil de usar que les permita reproducir su
música de manera gratuita, además de crear listas de reproducción y modificar los metadatos de sus
canciones consistentemente.    

A largo plazo, este tipo de aplicaciones tienen abiertas muchas posibilidades y se le podría añadir
nuevas funcionalidades que hagan más única a la aplicación y le den una identidad que le haga resaltar. 

### Requisitos funcionales
* R1: La aplicación debe reproducir música local y remota.
	* R1A: Debe ser capaz de pausar, reanudar y pasar las canciones.
	* R1B: El usuario debe poder reproducir canciones de forma aleatoria.
* R2: La aplicación debe establecer una base de datos sobre la Android Mediastore para cachear datos.
	* R2A: La base de datos debe tener preferencia sobre la Mediastore.
	* R2B: El usuario debe ser capaz de modificar estos datos.
	* R2C: La aplicación debería refrescar los datos de los metadatos de las canciones con los de la
	base de datos local.
* R3: La aplicación debe permitir al usuario trabajar con listas de reproducción.
	* R3A: El usuario debe ser capaz de crear y borrar listas de reproducción.
	* R3B: El usuario debe ser capaz de añadir y eliminar canciones de las listas de reproducción.
* R4: La aplicación debe permitir al usuario configurarla y persistir las preferencias del mismo
localmente.
* R5: La aplicación debería poder reproducir canciones de manera "gapless" o con cross-fading y otros
efectos.
* R6: La aplicación debería dar la posibilidad al usuario de reconfigurar el sink de audio manualmente.

### Requisitos no funcionales
* R7: La aplicación debería ofrecer al usuario información relevante de manera intuitiva.
* R8: La aplicación debería evitar hacer resampling siempre que sea posible.
* R9: La aplicación debería ser rápida a la hora de decodificar audio, buscar elementos de la base de
datos, etc.
* R10: La aplicación debe dar información al usuario sobre errores, sin detener la reproducción de las
canciones o la lista de reproducción.
* R11: La aplicación debe reproducir las canciones en segundo plano.
	* R11A: La aplicación debe interactuar con la API de Android para permitir al usuario controlar
	la reproducción mediante los media controls.
* R12: La aplicación debe poder decodificar la mayoría de formatos de audio.

### Recursos
#### Software
Lenguajes: Java, C.    
Librerías: Android API, JNI, librería estándar de Java, sqlite, ffmpeg.
Programas: cc, Android Studio, cmake, python, ffmpeg-kit.

#### Hardware
Ordenador con SO compatible con Android Studio.   
Teléfono Android con versión compatible con las librerías usadas.

## Diseño de la solución software
### Prototipado gráfico
#### Wireframe
![now playing](/res/img/wireframe/playing.png)
![playlist](/res/img/wireframe/playlist.png)    
![album](/res/img/wireframe/album.png)
![artist](/res/img/wireframe/artist.png)

#### Mockup
![now playing](/res/img/mockup/playing.png)
![playlist](/res/img/mockup/playlist.png)    
![album](/res/img/mockup/album.png)
![artist](/res/img/mockup/artist.png)

### Base de datos
## Diseño conceptual
![er](/res/img/db/ER.png)

## Modelo lógico
![lg](/res/img/db/LG.png)

## Implementación
### Codificación
La implementación de la aplicación se ha realizado en Java independientemente de si se trata del frontend
o no, usando las distintas librerías que aporta Android para el desarrollo de aplicaciones multimedia.

#### Backend
El backend de la aplicación se fundamenta en una clase que guarda el estado global del reproductor y a
la que las distintas actividades pueden acceder.

Esta clase se llama [Emp](/Emp/app/src/main/java/com/emp/Emp.java) y hereda de la clase `Application` por
defecto de Android, para asegurar que se inicializa antes que el resto de los componentes de la aplicación.
Dentro de esta clase se encuentra el handler de la base de datos de sqlite y el propio reproductor de
música.

El acceso a la base de datos se hace mediante una clase llamada [Connector](/Emp/app/src/main/java/com/emp/db/Connector.java)
y que se encarga de actualizar los datos de la base de datos de Android (MediaStore) con la base de datos
interna de la aplicación, y de ofrecer los métodos para extraer los distintos datos de la misma.

Como cualquier otra aplicación de Android, el backend se divide en las distintas actividades asociadas
a las distintas vistas del frontend. Todas ellas guardan referencias a los distintos elementos visuales
y se encargan de actualizar los datos en pantalla.

La actividad principal [MainActivity](/Emp/app/src/main/java/com/emp/MainActivity.java) es también la 
encargada de pedir al usuario los permisos necesarios para acceder a las bases de datos y crear los
archivos de ésta. Una vez se ha asegurado de que el usuario ha dado los permisos, recupera los datos de
la base de datos interna (si los hay), e inicializa la librería de la aplicación.

Para refrescar los elementos visuales con el estado en el que se encuentra el reproductor de música, las
distintas actividades implementan los métodos de la interfaz [PlayerCallback](/Emp/app/src/main/java/com/emp/audio/PlayerCallback.java)
la cual define métodos para notificar sobre cambios en el reproductor.

Por su parte, el reproductor se encuentra definido en [Player](/Emp/app/src/main/java/com/emp/audio/Player.java)
y realiza las distintas operaciones sobre el reproductor de música subyacente (una instancia de
`MediaPlayer`). Además, guarda una lista de las distintas actividades "clientes" a las que debe enviar
mensajes mediante la interfaz anterior cuando se produzcan cambios.

#### Frontend
El frontend de la aplicación es el típico encontrado en cualquier otra aplicación Android. Se encuentra
dividido en las distintas actividades y sus respectivas layouts, así como otros archivos secundarios que
sirven para los distintos componentes.

El layout de la [vista principal](/Emp/app/src/main/res/layout/activity_main.xml) por ejemplo es simplemente
un [RecyclerView](https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView) con
otros RecyclerViews secundarios dentro del mismo que se inicializan desde el backend.

Todos los layouts principales tienen asociados una barra de navegación que permite al usuario navegar entre
las vistas principales.

### Pruebas
Las pruebas unitarias están ubicadas en la carpeta [test](/Emp/app/src/test/), como es común en los proyectos
de Android Studio. Éstas hacen uso de la librería de JUnit4 para realizar los tests e informar de errores.

Dado que la aplicación no tiene un componente lógico fuerte, la clase de pruebas que se incorpora es sobre
la clase auxiliar [Time](/Emp/app/src/main/java/com/emp/utils/Time.java), y que se encarga de formatear el
timestamp de las canciones para su visualización. Esta clase puede encontrase en 
[TimeFormattingTest](/Emp/app/src/test/java/com/emp/TimeFormattingTest.java) y contiene tests básicos para
garantizar el correcto funcionamiento del código.

## Documentación
### Empaquetado
Dado que se trata de una aplicación de Android, la distribución de la aplicación se hace en formato
.apk. Para conseguir este archivo basta con compilar el proyecto con la configuración por defecto desde
Android Studio. El archivo se encontrará en la ruta `<proyecto>/pp/build/outputs/apk/debug/`.

El .apk generado por defecto se encuentra en modo debug, si lo que se quiere es generar un .apk firmado,
éste tiene que estar firmado con una clave. Para generarlo, basta con hacer click sobre
`Build > Generate Signed APK` y seguir los pasos.

### Instalación
La instalación de la aplicación se puede hacer mediante la herramienta `adb` desde el ordenador con el
comando: 

```bash
apt install <path_al_apk>
```

En el caso de que el .apk esté firmado, también puede instalarse mediante el instalador de paquetes
que tenga por defecto el dispositivo móvil.

## Conclusiones
Este proyecto me ha llevado más tiempo del que yo me esperaba inicialmente. Debido a la falta de tiempo,
errores relacionados con aspectos técnicos del proyecto y problemas con mi ordenador, que me llevaron a 
tener que empezar el proyecto de nuevo casi desde cero (porque se me olvidó subir los commits al repositorio
remoto de GitHub), el reproductor de música no está completo y tiene bugs importantes.

De los requisitos no completados, destacaría como más importantes el hecho de poder reproducir playlists,
utilizar servicios para poder enviar comandos mediante los [MediaControls](https://developer.android.com/media/implement/surfaces/mobile),
realizar búsquedas sobre la librería y controles básicos para pasar entre canciones.

Este último es probablemente el error más importante de todos y el que todavía no he conseguido averiguar, 
ya que se origina internamente dentro de la propia implementación de Android. Por desgracia, el hecho de poder
tener estos controles sobre el reproductor de música es esencial, no sólo de cara al usuario, sino también de
cara a la interacción con las listas de reproducción.

En cuanto a las listas de reproducción y más concretamente la "cola" de canciones, todo está implementado y
"técnicamente" debería funcionar, si no fuera por los errores mencionados anteriormente. La persistencia de
playlists y otros modelos de la base de datos tampoco están implementados aunque no llevaría mucho tiempo.

Es cierto que Android incorpora en las versiones más recientes la clase [Exoplayer](https://developer.android.com/media/media3/exoplayer)
y que resuelve la gran mayoría de los problemas anteriores: los controles, el servicio para recibir comandos,
la playlist asociada al reproductor, etc. Pero el objetivo de este proyecto también era el de intentar usar
lo menos posible clases y librerías que abstrajeran demasiado los conceptos principales de un reproductor de
música.

Otro aspecto importante que está ausente era la capacidad de descargar canciones mediante links de distintas
plataformas. En un principio esto se iba a conseguir fácilmente incorporando un binario de [youtube-dl](https://github.com/ytdl-org/youtube-dl)
o [yt-dlp](https://github.com/yt-dlp/yt-dlp) junto al ejecutable de la aplicación principal. Desgraciadamente
esto es bastante difícil desde el punto de vista de seguridad y de consistencia entre distintas arquitecturas
así que decidí usar la librería de [NewPipe Extractor](https://github.com/TeamNewPipe/NewPipeExtractor) que
permite conseguir los streams asociados a canciones de las principales plataformas sin hacer uso de sus APIs.  

Las librarías de NewPipe fueron probablemente unas de las partes que más tiempo consumió ya que la documentación
asociada a estas librerías es escasa y prácticamente inexistente, o destinada exclusivamente para el desarrollo
de nuevos extractores en lugar de a su uso. Todo esto para acabar luego con un error de ClassNotFoundException
asociado al hecho de que mi móvil no tiene la versión de SDK mínima compatible con estas librerías.

En todo caso, y a pesar de que no me siento a gusto con el resultado, sigue siendo una aplicación que hace uso
de distintos aspectos relacionados con la persistencia de datos, la interfaz gráfica de usuario, componentes
lógicos y elementos multimedia.

## Bibliografía
ffmpeg - https://ffmpeg.org/   
mpv - https://mpv.io/   
NewPipe - https://newpipe.net/   
Android MediaStore - https://developer.android.com/reference/android/provider/MediaStore    