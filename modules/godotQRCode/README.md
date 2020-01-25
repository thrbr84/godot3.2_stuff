Esse módulo foi feito para a Godot 3.2


### Como usar
----------

- Coloque essa pasta ```godotQRCode``` dentro do seu caminho ```res://android/```
- Abra as configurações do seu projeto -> Android -> Modules, e acrescente:

```
org/godotengine/godot/godotQRCode
```


Video com a explicação completa no meu canal do Youtube: 
- https://www.youtube.com/watch?v=Kq5MisDO26s

[![Video demonstração](https://img.youtube.com/vi/Kq5MisDO26s/0.jpg)](https://www.youtube.com/watch?v=Kq5MisDO26s)



### Configurações
-------------

- Dentro do diretório do módulo "godotQRCode/src" alterar o arquivo "godotQRCode.java" onde consta ```com.thiagobruno.godotqrcode``` pelo id da sua aplicação!

```
...
private String aplicationId = "com.thiagobruno.godotqrcode";
...
```

------

- Dentro do diretório do módulo "godotQRCode" alterar o arquivo "AndroidManifest.cong" onde consta ```com.thiagobruno.godotqrcode``` pelo id da sua aplicação!

```
...
android:authorities="com.thiagobruno.godotqrcode"
...
```

------

- Dentro do diretório do módulo "godotQRCode/res/xml" alterar o arquivo "file_paths.xml" onde consta ```com.thiagobruno.godotqrcode``` pelo id da sua aplicação!

```
...
<external-path name="my_images" path="Android/data/com.thiagobruno.godotqrcode/files/Pictures" />
...
```


### Como usar
-------------
- Utilize o exemplo godotQRCode para ver o funcionamento

### TODO
-------------
- Preview camera inside godot
- Preview with mask
- Crop and adjust de captured qrcode

### Exportar
-------------
- Permissões necessárias: CAMERA, READ/WRITE _EXTERNAL_STORAGE

### ...
Vai utilizar esse código de forma comercial? Fique tranquilo pode usar de forma livre e sem precisar mencionar nada, claro que vou ficar contente se pelo menos lembrar da ajuda e compartilhar com os amigos, rs. Caso sinta no coração, considere me pagar um cafezinho :heart: -> https://ko-fi.com/thiagobruno



