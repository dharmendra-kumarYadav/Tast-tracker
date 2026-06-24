# TaskTracker CLI

A simple Java CLI application for tracking tasks in a JSON file.

**Repository:** [https://github.com/dharmendra-kumarYadav/Tast-tracker](https://github.com/dharmendra-kumarYadav/Tast-tracker)

## Overview

This project provides a command line task tracker with support for:

- adding tasks
- updating tasks
- deleting tasks
- marking tasks as in progress
- marking tasks as done
- listing all tasks
- listing tasks by status (`todo`, `in-progress`, `done`)

Tasks are stored in `tasks.json` in the current working directory.

## Build

From the project root, run:

```powershell
mvn package
```

This generates the executable JAR file:

- `target/task-cli.jar`

## CLI wrappers

The project includes two wrapper files in the root directory:

- `task-cli.bat` for Windows
- `task-cli` for macOS/Linux

These wrappers launch the packaged JAR and forward command arguments.

## Run commands

Use one of the following forms after building the project.

### Windows

```powershell
.\task-cli.bat add "Buy groceries"
.\task-cli.bat update 1 "Buy groceries and cook dinner"
.\task-cli.bat delete 1
.\task-cli.bat mark-in-progress 1
.\task-cli.bat mark-done 1
.\task-cli.bat list
.\task-cli.bat list todo
.\task-cli.bat list in-progress
.\task-cli.bat list done
```

### macOS/Linux

```bash
./task-cli add "Buy groceries"
./task-cli update 1 "Buy groceries and cook dinner"
./task-cli delete 1
./task-cli mark-in-progress 1
./task-cli mark-done 1
./task-cli list
./task-cli list todo
./task-cli list in-progress
./task-cli list done
```

### Direct Java run

If you do not want to use the wrapper scripts, run:

```powershell
java -jar target/task-cli.jar add "Buy groceries"
```

## Command details

### `add "description"`

Adds a new task with the provided description.
- Status: `todo`
- `createdAt` and `updatedAt` are set to the current timestamp.
- Output: `Task added successfully (ID: <id>)`

### `update <id> "new description"`

Updates the task description for the task with the given ID.
- Updates `updatedAt`.
- Output: `Task updated successfully.`

### `delete <id>`

Deletes the task with the given ID.
- Output: `Task deleted successfully.`

### `mark-in-progress <id>`

Sets the task status to `in-progress`.
- Updates `updatedAt`.
- Output: `Task marked as in-progress successfully.`

### `mark-done <id>`

Sets the task status to `done`.
- Updates `updatedAt`.
- Output: `Task marked as done successfully.`

### `list`

Displays all tasks.

### `list todo`, `list in-progress`, `list done`

Displays tasks filtered by status.

## Behavior and errors

- **Missing ID:** When a command references a task ID that does not exist the CLI prints the following message:

```powershell
Error: id <n> not exist.
```

Example:

```powershell
.\task-cli.bat update 999 "doesn't exist"
# Output: Error: id 999 not exist.
```

- **Malformed or missing arguments:** If required arguments are missing or malformed the CLI prints a clear usage error. Example:

```powershell
./task-cli update 1" Bad
# Output: Error: The update command requires an ID and a new description.
```

## Making task-cli available globally

To run `task-cli` from any directory, either copy `task-cli.bat` into a folder on your PATH (for example `C:\Windows`) or add the project folder to your PATH. Example (PowerShell):

```powershell
setx PATH "%PATH%;C:\Users\ydhar\IdeaProjects\TaskTracker\"
```

Be careful when modifying PATH — prefer adding a dedicated scripts folder and placing the wrapper there.

## Data storage

The app stores tasks in `tasks.json` with the following fields:

- `id` — unique task identifier
- `description` — task description
- `status` — `todo`, `in-progress`, or `done`
- `createdAt` — timestamp when task was created
- `updatedAt` — timestamp when task was last updated

If `tasks.json` does not exist, it is created automatically.
