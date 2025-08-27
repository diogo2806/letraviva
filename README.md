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

## Integração com banco gratuito na nuvem

Este projeto utiliza o **Firebase Firestore** como exemplo de banco de dados na
nuvem. O serviço possui um plano gratuito que permite testes iniciais sem custos.

1. Acesse o [console do Firebase](https://console.firebase.google.com/) e crie
   um projeto.
2. Habilite o Firestore no modo *test* para facilitar os primeiros passos.
3. Crie uma coleção chamada `plans` contendo documentos com o campo `title`.
4. Copie o arquivo `google-services.json` gerado para o diretório `app/` do
   projeto.

O fragmento `PlanosFragment` carrega os planos diretamente do Firestore por meio
da classe `PlanosViewModel`. Ajuste a coleção conforme a necessidade do seu
aplicativo.

## Licença

Este projeto é distribuído sem uma licença específica. Utilize o código de acordo com as boas práticas de software livre.
