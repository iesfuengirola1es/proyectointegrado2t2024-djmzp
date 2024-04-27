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
### Modelado casos de uso

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
...

## Documentación
...

## Conclusiones
...

## Bibliografía
ffmpeg - https://ffmpeg.org/
mpv - https://mpv.io/
NewPipe - https://newpipe.net/
Android MediaStore - https://developer.android.com/reference/android/provider/MediaStore