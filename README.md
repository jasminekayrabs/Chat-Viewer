# Chat-Viewer
The chat-viewer application is a user-friendly GUI which converts .msg and .txt files to chat conversations by parsing them. The chat-viewer application uses a strong error handling technique that not only detects and manages errors effectively but also clearly displays error messages to users in a clear and user-friendly manner. This application also converts smileys (":)" and ":(") into happy and sad gifs.

# Requirements
[Java](https://www.java.com/en/)

# Functionality
The chat-viewer Application comprises various key entities, each responsible for specific tasks. The ChatController acts as the central hub, managing the user interface and coordinating interactions between other entities. It uses the 'handleOpenFile' method to load opened files. It uses the 'processFile' to process the files and validate their format and syntax. It also uses the 'showError' method to handle the exceptions. This method displays an error message in an alert dialog.

# Usage
Run project folder on a java environment. Select a txt or msg file from device and convert into chat conversations. The msg or txt file must contain content in the following format: [Time stamp]
        [Nickname]
        [Content]

# MIT License
Copyright (c) 2023 jasminekayrabs
