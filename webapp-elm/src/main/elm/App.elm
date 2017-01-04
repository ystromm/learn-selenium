import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (..)
--import Html.Events.Extra exposing (..)
import String
import Http exposing (...)
import Json.Decode as Decode
import Task exposing (..)

type alias Todo =
  { text : String
  , id : Int,
  done: Bool
  }

getTodos : Http.Request List Todo
getTodos =
  Http.get "http://localhost:8080/api/todos decodeTodos

decodeTodos: Decode.Decoder List Todo
decodeTodos = Decode.map2 Todo
    (Decode.field "done" Decode.boolean)
    (Decode.field "text" Decode.string)
    (Decode.field "id" Decode.int)

    getRandomGif : String -> Cmd Msg
    getRandomGif topic =
      let
        url =
          "https://api.giphy.com/v1/gifs/random?api_key=dc6zaTOxFJmzC&tag=" ++ topic

        request =
          Http.get url decodeGifUrl
      in
        Http.send NewGif request

    decodeGifUrl : Json.Decoder String
    decodeGifUrl =
      Json.at ["data", "image_url"] Json.string

--init

main =
        ul [attribute "data-test-id" "todos_items_ul"] [
           li [attribute "data-test-id" "todos_items_li"] [text "Don't forget!"]
        ]


--view