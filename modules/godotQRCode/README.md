Esse módulo foi feito para a Godot 3.2 - talvez funcione na versão 3.5 mas não foi testado


### Como usar
----------

1 - Clique no menu: Project > Install Android Build Template
2 - Edite o arquivo: ```android\build\gradle\wrapper\gradle-wrapper.properties``` e altere a distribuição para: ```distributionUrl=https\://services.gradle.org/distributions/gradle-6.7.1-all.zip```
3 - Edite o arquivo: ```android\build\config.gradle``` e coloque o minSdk para 24
4 - Edite o arquivo: ```android\build\gradle.config``` e adicione as duas linhas abaixo no fim do arquivo

```
android.useAndroidX=true
android.enableJetifier=true
```

5 - Coloque essa pasta ```godotQRCode``` dentro do seu caminho ```res://android/```
6 - Abra as configurações do seu projeto -> Android -> Modules, e acrescente:

```
org/godotengine/godot/godotQRCode
```

7 - Dentro do diretório do módulo "godotQRCode/src" alterar onde consta ```com.thiagobruno.godotqrcode``` pelo id da sua aplicação!



### Como usar
-------------
- Utilize o exemplo godotQRCode para ver o funcionamento

### TODO
-------------
- Alterar o título que aparece na camera

### Exportar
-------------
- Permissões necessárias: CAMERA

### ...
Vai utilizar esse código de forma comercial? Fique tranquilo pode usar de forma livre e sem precisar mencionar nada, claro que vou ficar contente se pelo menos lembrar da ajuda e compartilhar com os amigos, rs. Caso sinta no coração, considere me pagar um cafezinho :heart: -> https://ko-fi.com/thsbruno



