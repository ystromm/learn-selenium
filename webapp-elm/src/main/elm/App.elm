module Main exposing (..)

import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (..)


--import Html.Events.Extra exposing (..)

import String
import Http
import Maybe exposing (withDefault)
import Json.Decode as Decode
import Json.Encode as Encode
import Task exposing (..)


type alias Flags =
    { todoUrl : String
    }


type alias Todo =
    { id : Int
    , text : String
    , done : Bool
    }


type alias Model =
    { todoUrl : String
    , todos : List Todo
    , text : String
    , reason : String
    }


type Msg
    = Add
    | Text String
    | Delete Todo
    | Done Todo
    | Created (Result Http.Error Todo)
    | Updated (Result Http.Error Todo)
    | Deleted (Result Http.Error Int)
    | Get (Result Http.Error (List Todo))


main =
    Html.programWithFlags
        { init = init
        , view = view
        , update = update
        , subscriptions = subscriptions
        }


init : Maybe Flags -> ( Model, Cmd Msg )
init flags =
    let
        withDefaultFlags =
            withDefault (Flags "http://localhost:8081/api/todo") flags
    in
        ( Model withDefaultFlags.todoUrl [] "" "", getTodos withDefaultFlags.todoUrl )


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        Get (Ok todos) ->
            ( { model | todos = todos }, Cmd.none )

        Get (Err error) ->
            ( { model | reason = errorMessage error }, Cmd.none )

        Created (Ok todo) ->
            ( add model todo, Cmd.none )

        Created (Err error) ->
            ( { model | reason = errorMessage error }, Cmd.none )

        Updated (Ok todo) ->
            ( model, getTodos model.todoUrl )

        Updated (Err error) ->
            ( { model | reason = errorMessage error }, Cmd.none )

        Deleted (Ok id) ->
            ( delete model id, Cmd.none )

        Deleted (Err error) ->
            ( { model | reason = errorMessage error }, Cmd.none )

        Text text ->
            ( { model | text = text }, Cmd.none )

        Add ->
            ( model, createTodo model.todoUrl model.text )

        Done todo ->
            ( model, updateTodo model.todoUrl { todo | done = not todo.done } )

        Delete todo ->
            ( model, deleteTodo model.todoUrl todo )


view : Model -> Html Msg
view model =
    div []
        [ div [ attribute "data-test-id" "todos_items_ul" ]
            (List.map
                (\todo ->
                    div [ attribute "data-test-id" "todos_items_li" ]
                        [ input [ data_test_id "todo_done_checkbox" todo.id, type_ "checkbox", checked todo.done, onClick (Done todo) ] []
                        , input [ data_test_id "todo_text_text" todo.id, type_ "text", value todo.text ] []
                        , button [ data_test_id "todo_delete_button" todo.id, class "btn btn-default glyphicon glyphicon-remove", onClick (Delete todo) ] []
                        ]
                )
                model.todos
            )
        , input [ attribute "data-test-id" "todo_add_text", type_ "text", placeholder "New todo", onInput Text, value model.text ] []
        , button [ attribute "data-test-id" "todo_add_button", class "btn btn-default glyphicon glyphicon-plus", onClick Add ] []
        , div [] [ text model.reason ]
        ]


data_test_id : String -> Int -> Attribute msg
data_test_id name id =
    attribute "data-test-id" (name ++ "_" ++ (toString id))


subscriptions : Model -> Sub Msg
subscriptions model =
    Sub.none


done : Model -> Int -> Model
done model id =
    model


delete : Model -> Int -> Model
delete model id =
    Model model.todoUrl (List.filter (\todo -> todo.id /= id) model.todos) model.text ""


add : Model -> Todo -> Model
add model todo =
    Model model.todoUrl (todo :: model.todos) "" ""


errorMessage : Http.Error -> String
errorMessage error =
    case error of
        Http.BadUrl message ->
            message

        Http.Timeout ->
            "Timeout"

        Http.NetworkError ->
            "Network error"

        Http.BadPayload message _ ->
            message

        Http.BadStatus response ->
            response.status.message


getTodos : String -> Cmd Msg
getTodos todoUrl =
    let
        url =
            todoUrl

        request =
            Http.get url decodeTodos
    in
        Http.send Get request


deleteTodo : String -> Todo -> Cmd Msg
deleteTodo todoUrl todo =
    let
        url =
            todoUrl ++ "/" ++ (toString todo.id)

        request =
            Http.request
                { method = "delete"
                , headers = []
                , url = url
                , body = Http.jsonBody <| encodeTodo todo
                , expect = Http.expectStringResponse (\_ -> Ok (todo.id))
                , timeout = Nothing
                , withCredentials = False
                }
    in
        Http.send Deleted request


updateTodo : String -> Todo -> Cmd Msg
updateTodo todoUrl todo =
    let
        url =
            todoUrl ++ "/" ++ (toString todo.id)

        request =
            Http.request
                { method = "put"
                , headers = []
                , url = url
                , body = Http.jsonBody <| encodeTodo todo
                , expect = Http.expectJson decodeTodo
                , timeout = Nothing
                , withCredentials = False
                }
    in
        Http.send Updated request


createTodo : String -> String -> Cmd Msg
createTodo todoUrl text =
    let
        url =
            todoUrl

        todo =
            Todo 0 text False

        request =
            Http.request
                { method = "post"
                , headers = []
                , url = url
                , body = Http.jsonBody <| encodeTodo todo
                , expect = Http.expectJson decodeTodo
                , timeout = Nothing
                , withCredentials = False
                }
    in
        Http.send Created request


decodeTodos : Decode.Decoder (List Todo)
decodeTodos =
    Decode.list decodeTodo


decodeTodo : Decode.Decoder Todo
decodeTodo =
    Decode.map3 Todo
        (Decode.field "id" Decode.int)
        (Decode.field "text" Decode.string)
        (Decode.field "done" Decode.bool)


encodeTodo : Todo -> Encode.Value
encodeTodo todo =
    Encode.object
        [ ( "text", (Encode.string todo.text) )
        , ( "id", (Encode.int todo.id) )
        , ( "done", (Encode.bool todo.done) )
        ]
