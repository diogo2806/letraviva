# Letra Viva

Este repositório contém o código do aplicativo **Letra Viva**, um aplicativo Android focado em leituras e devocionais bíblicos. A proposta é oferecer planos de estudo, áudios narrados e interação simplificada para fortalecer a vida espiritual do usuário.

## Funcionalidades principais

- Autenticação com Google
- Lista de planos de leitura
- Devocionais em texto e áudio
- Reprodução de áudios locais
- Configurações básicas do aplicativo

## Como executar

1. Instale o [Android Studio](https://developer.android.com/studio).
2. Clone este repositório e abra o projeto pelo Android Studio.
3. Aguarde a sincronização do Gradle e conecte um dispositivo ou inicie um emulador.
4. Utilize a opção **Run** do Android Studio para compilar e instalar o aplicativo.
5. Caso os scripts `gradlew` não estejam presentes, execute `gradle wrapper` para gerá-los.
6. Crie um projeto no Firebase e coloque o arquivo `google-services.json` em `app/`. Atualize também o valor de `default_web_client_id` em `app/src/main/res/values/strings.xml`.

Para compilar via linha de comando você também pode executar:

```bash
./gradlew assembleDebug
```

O APK será gerado em `app/build/outputs/apk/`.

## Estrutura do projeto

O módulo principal fica em `app/` e segue a organização padrão de um aplicativo Android. Os pacotes dentro de `app/src/main/java/br/com/valenstech/letraviva` contêm:

- `ui/` – atividades e fragments da interface
- `viewmodel/` – classes de ViewModel
- `model/` – modelos de dados utilizados no app
- `util/` – constantes e utilidades diversas

## Licença

Este projeto é distribuído sem uma licença específica. Utilize o código de acordo com as boas práticas de software livre.
