Para Godot 3.2 - para iOS use esse link [old module](https://github.com/Shin-NiL/Godot-Share). 

### Como usar
----------

- Coloque essa pasta ```share-plugin``` dentro do seu caminho ```res://android/```
- Abra as configurações do seu projeto -> Android -> Modules, e acrescente:

```
org/godotengine/godot/GodotShare
```


### API Reference
-------------

The following methods are available:
```python
# Share text
# @param String title
# @param String subject
# @param String text
shareText(title, subject, text)

# Share image
# @param String image_abs_path The image location absolute path
# @param String title
# @param String subject
# @param String text
void sharePic(image_abs_path, title, subject, text)
```


### Referência:
- https://github.com/Shin-NiL/Godot-Share
