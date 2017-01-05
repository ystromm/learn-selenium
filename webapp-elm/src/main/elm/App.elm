import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (..)
--import Html.Events.Extra exposing (..)
import String
import Http
import Json.Decode as Decode
import Json.Encode as Encode
import Task exposing (..)

type alias Todo = {
    id: Int,
    text : String,
    done: Bool
  }

type alias Model =
  {
      todos: List Todo,
      text: String,
      reason: String
  }

type Msg
  = Add
  | Text String
  | Delete Todo
  | Done Todo
  | Created (Result Http.Error Todo)
  | Updated (Result Http.Error Todo)
  | Deleted (Result Http.Error Todo)
  | Get (Result Http.Error (List Todo))

main =
  Html.program
    {
        init = init,
        view = view,
        update = update,
        subscriptions = subscriptions
    }

init : (Model, Cmd Msg)
init = (Model [ ] "" "", getTodos)

update : Msg -> Model -> (Model, Cmd Msg)
update msg model =
    case msg of
        Get (Ok todos) ->
            ({model | todos = todos }, Cmd.none)
        Get (Err error) ->
            ({model | reason = errorMessage error}, Cmd.none)
        Created (Ok todo) ->
             (add model todo, Cmd.none)
        Created (Err error) ->
            ({model | reason = errorMessage error}, Cmd.none)
        Updated (Ok todo) ->
             (model, getTodos)
        Updated (Err error) ->
            ({model | reason = errorMessage error}, Cmd.none)
        Deleted (Ok todo) ->
             (delete model todo.id, Cmd.none)
        Deleted (Err error) ->
            ({model | reason = errorMessage error}, Cmd.none)
        Text text ->
            ({model | text = text}, Cmd.none)
        Add ->
            (model, createTodo model.text)
        Delete todo ->
            (model, deleteTodo todo)
        Done todo ->
            (model, updateTodo {todo | done = not todo.done})

view : Model -> Html Msg
view model =
        div [] [
        div [attribute "data-test-id" "todos_items_ul"]
           (List.map (\ todo ->
               div [attribute "data-test-id" "todos_items_li"] [
                       input [ type_ "checkbox", checked todo.done, onClick (Done todo) ] [],
                       input [ type_ "text", value todo.text] [],
                       button [ class "btn btn-default glyphicon glyphicon-remove", onClick (Delete todo)] []
                       ]
           ) model.todos),
           input [ type_ "text", placeholder "New todo", onInput Text, value model.text] [],
           button [ class "btn btn-default glyphicon glyphicon-plus", onClick Add] [],
           div [] [text model.reason]
           ]

subscriptions : Model -> Sub Msg
subscriptions model =
  Sub.none

done : Model -> Int -> Model
done model id = model

delete : Model -> Int -> Model
delete model id = Model (List.filter (\ todo -> todo.id /= id) model.todos) model.text ""

add : Model -> Todo -> Model
add model todo = Model (todo :: model.todos) "" ""

errorMessage: Http.Error -> String
errorMessage error =
    case error of
    Http.BadUrl message -> message
    Http.Timeout -> "Timeout"
    Http.NetworkError -> "Network error"
    Http.BadPayload message _ -> message
    Http.BadStatus response -> response.status.message

getTodos : Cmd Msg
getTodos =
    let
        url = "http://localhost:8081/api/todo"
        request = Http.get url decodeTodos
    in
        Http.send Get request

deleteTodo : Todo -> Cmd Msg
deleteTodo todo =
    let
        url = "http://localhost:8081/api/todo/" ++ (toString todo.id)
        request = Http.request {
            method = "delete",
            headers = [],
            url = url,
            body = Http.jsonBody <| encodeTodo todo,
            expect = Http.expectJson decodeTodo,
            timeout = Nothing,
            withCredentials = False}
    in
        Http.send Deleted request

updateTodo : Todo -> Cmd Msg
updateTodo todo =
    let
        url = "http://localhost:8081/api/todo/" ++ (toString todo.id)
        request = Http.request {
            method = "put",
            headers = [],
            url = url,
            body = Http.jsonBody <| encodeTodo todo,
            expect = Http.expectJson decodeTodo,
            timeout = Nothing,
            withCredentials = False}
    in
        Http.send Updated request

createTodo : String -> Cmd Msg
createTodo text =
    let
        url = "http://localhost:8081/api/todo"
        todo = Todo 0 text False
        request = Http.request {
            method = "post",
            headers = [],
            url = url,
            body = Http.jsonBody <| encodeTodo todo,
            expect = Http.expectJson decodeTodo,
            timeout = Nothing,
            withCredentials = False}
    in
        Http.send Created request



decodeTodos: Decode.Decoder (List Todo)
decodeTodos = Decode.list decodeTodo

decodeTodo: Decode.Decoder Todo
decodeTodo = Decode.map3 Todo
    (Decode.field "id" Decode.int)
    (Decode.field "text" Decode.string)
    (Decode.field "done" Decode.bool)

encodeTodo: Todo -> Encode.Value
encodeTodo todo = Encode.object
   [("text", (Encode.string todo.text)),
   ("id", (Encode.int todo.id)),
   ("done", (Encode.bool todo.done)) ]


