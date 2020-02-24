Para Godot 2.1 - use esse link [old module](https://github.com/thiagobruno/godotCamera). 


Esse módulo está adaptado para a Godot 3.2


### Como usar
----------

- Coloque essa pasta ```godotCamera``` dentro do seu caminho ```res://android/```
- Abra as configurações do seu projeto -> Android -> Modules, e acrescente:

```
org/godotengine/godot/GodotCamera
```


Video com a explicação completa no meu canal do Youtube: 
- https://www.youtube.com/watch?v=k22wTP1qD1o

[![Video demonstração](https://img.youtube.com/vi/k22wTP1qD1o/0.jpg)](https://www.youtube.com/watch?v=k22wTP1qD1o)



### Configurações
-------------

- Após instalar o  modelo de compilação android, edite o arquivo: ```res://android/build/gradle.properties``` e acrescente no fim do arquivo essas duas linhas:
```bash
android.useAndroidX=true
android.enableJetifier=true
```


- Dentro do diretório do módulo "godotCamera/src" alterar o arquivo "GodotCamera.java" onde consta ```com.thiagobruno.godotcamera``` pelo id da sua aplicação!

```
...
private String aplicationId = "com.thiagobruno.godotcamera";
...
```

------

- Dentro do diretório do módulo "godotCamera" alterar o arquivo "AndroidManifest.cong" onde consta ```com.thiagobruno.godotcamera``` pelo id da sua aplicação!

```
...
android:authorities="com.thiagobruno.godotcamera"
...
```

------

- Dentro do diretório do módulo "godotCamera/res/xml" alterar o arquivo "file_paths.xml" onde consta ```com.thiagobruno.godotcamera``` pelo id da sua aplicação!

```
...
<external-path name="my_images" path="Android/data/com.thiagobruno.godotcamera/files/Pictures" />
...
```


### Como usar
-------------
- Utilize o exemplo GodotCamera para ver o funcionamento

### TODO
-------------
- AI / Face detect, recognize

### Exportar
-------------
- Permissões necessárias: CAMERA, READ/WRITE _EXTERNAL_STORAGE

### ...
Vai utilizar esse código de forma comercial? Fique tranquilo pode usar de forma livre e sem precisar mencionar nada, claro que vou ficar contente se pelo menos lembrar da ajuda e compartilhar com os amigos, rs. Caso sinta no coração, considere me pagar um cafezinho :heart: -> https://ko-fi.com/thiagobruno



