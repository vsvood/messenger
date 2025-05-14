# Android Messenger Client - README

## Overview
This project is a simple Android messenger client that connects to a predefined server API. It allows users to:
- View a list of available chats
- Create new chats
- View messages in each chat
- Send new messages to chats

## API Description
The client communicates with the following REST API endpoints:

### Chat Operations
- **GET /mipt_network/chats**
    - Returns a JSON array of all available chats
    - Each chat object contains `id` and `name` fields

- **POST /mipt_network/create_chat**
    - Creates a new chat
    - Required parameter: `name` (string)
    - Returns updated list of all chats

### Message Operations
- **GET /mipt_network/chat**
    - Returns messages from a specific chat
    - Required parameter: `id` (int) - chat ID
    - Response includes:
        - `id`: chat ID
        - `name`: chat name
        - `messages`: array of message objects (each with `id` and `text`)

- **POST /mipt_network/msg**
    - Adds a new message to a chat
    - Required parameters:
        - `id` (int): chat ID
        - `text` (string): message content
    - Returns updated list of messages in the chat

## Project Structure

### Main Components

1. **Activities**
    - `MessengerActivity`: Main activity that hosts all fragments

2. **Fragments**
    - `ChatListFragment`: Displays list of available chats
    - `ChatFragment`: Shows messages in a specific chat
    - `NewMessageEnterFragment`: Input field for sending new messages

3. **Adapters**
    - `ChatListAdapter`: Handles chat list display in RecyclerView
    - `ChatMessageAdapter`: Handles message list display in RecyclerView

4. **Network**
    - `NetworkClient`: Retrofit client setup
    - `ChatApiService`: API interface definition
    - `AuthenticationInterceptor`: Adds OAuth header to requests

5. **Models**
    - `ChatEntity`: Data class for chat information
    - `MessageEntity`: Data class for message information

### Key Features

1. **Chat List**
    - Displays all available chats in a scrollable list
    - Each chat shows its name and a default icon
    - Floating action button to create new chats
    - Clicking a chat opens its message view

2. **Message View**
    - Shows all messages in the selected chat
    - Automatically scrolls to latest message when sending
    - Preserves scroll position when rotating device

3. **Message Sending**
    - Text input field with send button
    - Validates message is not empty before sending
    - Clears input field after successful send

4. **Network Operations**
    - All API calls are made asynchronously using coroutines
    - Error handling with Toast notifications
    - Authentication via OAuth header

## Implementation Details

### RecyclerView Adapters

1. **ChatListAdapter**
    - Binds chat data to RecyclerView items
    - Handles click events to open chat fragments
    - Uses Glide to load default chat icons
    - Supports updating chat list dynamically

2. **ChatMessageAdapter**
    - Binds message data to RecyclerView items
    - Supports updating message list dynamically

### Fragment Navigation

- Uses FragmentManager to handle navigation between fragments
- Implements back stack for proper back navigation
- Supports both single-pane and two-pane layouts (phone/tablet)

### Data Flow

1. When opening the app:
    - `MessengerActivity` loads `ChatListFragment`
    - `ChatListFragment` fetches chats via API and displays them

2. When opening a chat:
    - Click handler in `ChatListAdapter` opens `ChatFragment`
    - `ChatFragment` fetches messages for the selected chat
    - `NewMessageEnterFragment` is added for message input

3. When sending a message:
    - `NewMessageEnterFragment` captures input text
    - Calls `onMessageSend` callback in parent `ChatFragment`
    - `ChatFragment` sends message via API and updates view

## Setup Instructions

1. **Server Configuration**
    - Update `BASE_SERVER_URL` in `com.easy.messenger` package
    - Ensure API endpoints match the server implementation

2. **Authentication**
    - Modify `AuthenticationInterceptor` if different auth method is needed
    - Current implementation uses hardcoded OAuth token

3. **Build Requirements**
    - Android Studio
    - Minimum SDK version not specified (check build.gradle)

## Dependencies

The project uses:
- Retrofit for network calls
- Gson for JSON serialization
- Glide for image loading
- AndroidX libraries (Fragments, RecyclerView, etc.)
- Material Design components

## Known Limitations

1. UI/UX
    - Basic UI without advanced styling
    - No user differentiation in messages
    - No timestamps or read status indicators

2. Functionality
    - No real-time updates (polling not implemented)
    - No message editing/deletion
    - No user authentication flow

3. Error Handling
    - Basic error notifications via Toast
    - No retry mechanism for failed requests