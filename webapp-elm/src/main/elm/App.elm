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
      todos: List Todo,
      text: String,
      id: Int
  }

type Msg
  = Add
  | Text String
  | Delete Int
  | Done Int

main =
  Html.program
    { init = init
    , view = view
    , update = update
    , subscriptions = subscriptions
    }

init : (Model, Cmd Msg )
init = (Model [ Todo 1 "Don't forget" True, Todo 2 "Fuhgettaboutit" False ] "" 3, Cmd.none )

update : Msg -> Model -> (Model, Cmd Msg)
update msg model =
  case msg of
    Text text ->
        ({model | text = text}, Cmd.none)
    Add ->
      (add model, Cmd.none)
    Delete id ->
      (delete model id, Cmd.none)
    Done id ->
      (done model id, Cmd.none)

view : Model -> Html Msg
view model =
        div [] [
        div [attribute "data-test-id" "todos_items_ul"]
           (List.map (\ todo ->
               div [attribute "data-test-id" "todos_items_li"] [
                       input [ type_ "checkbox", checked todo.done, onClick (Done todo.id) ] [],
                       input [ type_ "text", value todo.text] [],
                       button [ class "btn btn-default glyphicon glyphicon-remove", onClick (Delete todo.id)] []
                       ]
           ) model.todos),
           input [ type_ "text", placeholder "New todo", onInput Text, value model.text] [],
           button [ class "btn btn-default glyphicon glyphicon-plus", onClick Add] []
           ]

subscriptions : Model -> Sub Msg
subscriptions model =
  Sub.none

add : Model -> Model
add model = Model ((Todo model.id model.text False) :: model.todos) "" (model.id+1)

done : Model -> Int -> Model
done model id = model

delete : Model -> Int -> Model
delete model id = Model (List.filter (\ todo -> todo.id /= id) model.todos) model.text model.id

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

