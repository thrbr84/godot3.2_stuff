# Godot Stuff


### Compilar Módulo: Godot 3.2.1

### Explicação
Video com a explicação completa no meu canal do Youtube: 
- https://www.youtube.com/watch?v=ku-6pFTQ6D0

[![Video com a explicação](https://img.youtube.com/vi/ku-6pFTQ6D0/0.jpg)](https://www.youtube.com/watch?v=ku-6pFTQ6D0)


Se ainda não viu o vídeo anterior, onde mostro os primeiros passos para montar o ambiente android no Ubuntu 18, veja pelo link abaixo:

### (1) COMO PREPARAR O AMBIENTE (SDK, JAVA) - Parte 1
https://www.youtube.com/watch?v=GUXQVwFlOMg

----------

### (2) COMO CONFIGURAR NDK
https://www.youtube.com/watch?v=Exm2Em4P_nk

----------

### (3) CONFIGURAR OS MÓDULOS QUE FOR USAR (pode usar todos):
#### GodotGPS
- Veja as configurações necessárias dentro do README que está no diretório [GodotGps](modules/godotgps/)

#### GodotNFC [WIP] ```em desenvolvimento```
- Veja as configurações necessárias dentro do README que está no diretório [GodotNfc](modules/godotNfc/)

#### GodotQRCode [WIP]
- Veja as configurações necessárias dentro do README que está no diretório [GodotQRCode](modules/godotQRCode)

#### GodotCamera [WIP]
- Veja as configurações necessárias dentro do README que está no diretório [GodotCamera](modules/godotCamera)

#### GodotBluetooth [WIP]
- Veja as configurações necessárias dentro do README que está no diretório [GodotBluetooth](modules/godotBluetooth)

#### AdMod (Somente versão da Godot 3.2.1)
- Criar conta em: https://www.google.com/intl/pt_br/admob/
- Configurar os módulos com os IDs dos seus banners
- Colocar nas propriedades do projeto: ```org/godotengine/godot/GodotAdMob```

#### Facebook
- Criar conta em: https://developers.facebook.com/
- Criar o aplicativo para seu jogo, e obter o ID do Facebook
- Em produtos, inclua: Login do Facebook e pegue as configurações informadas para colocar corretamente no seu projeto.
- Colocar nas propriedades do projeto: ```org/godotengine/godot/GodotFacebook```

#### Google Play Services
- Criar conta na Google Play conforme mostro nesse video: https://www.youtube.com/watch?v=dAJF7GhD_UU
- Configurar os "serviços relacionados a jogos" para seu jogo
- Configurar nos módulos seus IDs
- Colocar nas propriedades do projeto: ```org/godotengine/godot/GooglePlay,org/godotengine/godot/SQLBridge```

*Se for usar todos,.. em configurações do projeto/Anrdoid, coloque todos os módulos separados por vírgula "," - exemplo:
```org/godotengine/godot/GooglePlay,org/godotengine/godot/SQLBridge,org/godotengine/godot/GodotFacebook,org/godotengine/godot/GodotAdMob```

# Referência
Alguns módulos foram customizados e modificados baseados nos trabalhos dos autores:
- https://github.com/Shin-NiL/godot-admob
- https://github.com/DrMoriarty/godot-facebook
- https://github.com/FrogSquare/GodotGoogleService
- https://github.com/favarete/GodotBluetooth

# IDs para teste do ADMob
- https://developers.google.com/admob/android/test-ads

- Banner: ca-app-pub-3940256099942544/6300978111
- Interstitial: ca-app-pub-3940256099942544/1033173712
- Interstitial Video: ca-app-pub-3940256099942544/8691691433
- Rewarded Video: ca-app-pub-3940256099942544/5224354917
- Native Advanced: ca-app-pub-3940256099942544/2247696110
- Native Advanced Video: ca-app-pub-3940256099942544/1044960115

----------

# Mais...

- [Grass Path2D](https://github.com/thrbr84/godot_TBP_GrassPath2D)
- [Analog Controller](https://github.com/thrbr84/godot_analogcontroller)
- [Godot Laravel](https://github.com/thrbr84/godot_laravel)
- [Bouyancy Water Objetc](https://github.com/thrbr84/godot_buoyancyWaterObject)
- [Drag N Drop / Inventary](https://github.com/thrbr84/godot_dragndrop)
- [Accelerometer](https://github.com/thrbr84/godot_accelerometer)
- [INAPP](https://github.com/thrbr84/godot_inapp)
- [TextureRect URL](https://github.com/thrbr84/godot_texturerecturl)
- [Locale](https://github.com/thrbr84/godot_locale)
- [Save Local](https://github.com/thrbr84/godot_savelocal)
- [Countdown](https://github.com/thrbr84/godot3.2_countdown)
- [Lollipop Game](https://github.com/thrbr84/godot_lollipop)
- [Lançamento Obliquo](https://github.com/thrbr84/godot_lancamentoobliquo)
- [Compilar Modulo](https://github.com/thrbr84/godot3.1_compilarmodulo)
- [8 Directions movement pre-render](https://github.com/thrbr84/godot-8-directions-movement)



### ...
Vai utilizar esse código de forma comercial? Fique tranquilo pode usar de forma livre e sem precisar mencionar nada, claro que vou ficar contente se pelo menos lembrar da ajuda e compartilhar com os amigos, rs. Caso sinta no coração, considere me pagar um cafezinho :heart: -> https://ko-fi.com/thiagobruno



