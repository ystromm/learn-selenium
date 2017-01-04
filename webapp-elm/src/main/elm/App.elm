import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (..)
--import Html.Events.Extra exposing (..)
import String
--import Http exposing (...)
import Json.Decode as Decode
import Task exposing (..)


type alias Todo =
  { id: Int,
  text : String,
  done: Bool
  }

type alias Model =
  {
      todos: List Todo
  }

type Msg
  = MorePlease
--  | NewGif (Result Http.Error String)

main =
  Html.program
    { init = init
    , view = view
    , update = update
    , subscriptions = subscriptions
    }

init : (Model, Cmd Msg )
init = (Model [ Todo 2 "Fuhgettaboutit" False, Todo 1 "Don't forget" True ] , Cmd.none )

update : Msg -> Model -> (Model, Cmd Msg)
update msg model =
  case msg of
    MorePlease ->
      (model, Cmd.none)

--    NewGif (Ok newUrl) ->
--      (model Cmd.none)

--    NewGif (Err _) ->
--      (model, Cmd.none)

view : Model -> Html Msg
view model =
        div [attribute "data-test-id" "todos_items_ul"]
           (List.map (\ todo ->
               div [attribute "data-test-id" "todos_items_li"] [
                       input [ type_ "checkbox", checked todo.done] [],
                       input [ type_ "text", value todo.text] [],
                       button [ class "btn btn-default glyphicon glyphicon-remove" ] []
                       ]
           ) model.todos)

subscriptions : Model -> Sub Msg
subscriptions model =
  Sub.none

-- getTodos : Http.Request List Todo
-- getTodos =
--  Http.get "http://localhost:8080/api/todos decodeTodos

--decodeTodos: Decode.Decoder List Todo
--decodeTodos = Decode.map2 Todo
--    (Decode.field "done" Decode.boolean)
--    (Decode.field "text" Decode.string)
--    (Decode.field "id" Decode.int)

--    getRandomGif : String -> Cmd Msg
--    getRandomGif topic =
--      let
--        url =
--          "https://api.giphy.com/v1/gifs/random?api_key=dc6zaTOxFJmzC&tag=" ++ topic

--        request =
--          Http.get url decodeGifUrl
--      in
--        Http.send NewGif request

--    decodeGifUrl : Json.Decoder String
--    decodeGifUrl =
--      Json.at ["data", "image_url"] Json.string

