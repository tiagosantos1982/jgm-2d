#Project Overview
This project is a 2D game prototype for study.

#Folder Structure
- .github/
    - copilot-instructions.md
- src\br\com\jumpman
    - fx
    - screens
    - timer
    - .gitignore

## Coding Standards.
- Usar acentuações nos comentários e textos em strings desde que o arquivo esteja aberto em UTF-8, caso não esteja faça a mudança do encode primeiro.
- Use double quotes for strings.
- methods and ifs must be idented like below:
    :methods
        method_name( <params...>)
        {
            //
        }

        if( condition )
        {

        }
- do not use tab use 4 space for identations.

- this project use JDK 21+

- every successful building make a commit with a resumed text of what it's done. max of 30 words.

- enums must be added in a package called data.enums under the main package.
- all classes must be in a package.
- all coding must follow the best practices like SOLID and Clean Code.

- classes que implementam AbstractEntity devem estar dentro do pacote br.com.jumpman.entities. 
  se necessário pode se criar novo nível no pacote para atendender as boas práticas.

## IMPONTANTISSIMO ##
- Nenhuma classe deve estar fora de pacote e no pacote br.com.jumpman deverá existir apenas a classe de aplicação. No caso a App.java.