import type { Component } from 'solid-js';
import { TodoItemConfig } from "../types/TodoItemConfig";
import {For} from "solid-js";

const TodoItem: Component = (name: String, todo: TodoItemConfig[]) => {
  return (
    <div>
      <h2>{ name } todo list</h2>
        <Switch>
            <Match when={todo.length == 0}>
                <p>No items</p>
            </Match>
            <Match when={todo.length > 0}>
                <For each={todo}>{(item) =>
                    <ul>
                        <li>
                            {/*{ item.order }.*/}
                            { item.content }
                            { item.description }
                        </li>
                    </ul>
                }</For>
            </Match>
        </Switch>

    </div>
  );
};

export default TodoItem;
