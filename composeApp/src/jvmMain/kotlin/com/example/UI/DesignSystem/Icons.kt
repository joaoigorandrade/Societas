package com.example.UI.DesignSystem

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class SocietasIcons(
    val filled: ImageVector,
    val outlined: ImageVector,
    val rounded: ImageVector
) {
    // Navigation Icons
    HOME(
        filled = Icons.Filled.Home,
        outlined = Icons.Outlined.Home,
        rounded = Icons.Rounded.Home
    ),
    SEARCH(
        filled = Icons.Filled.Search,
        outlined = Icons.Outlined.Search,
        rounded = Icons.Rounded.Search
    ),
    SETTINGS(
        filled = Icons.Filled.Settings,
        outlined = Icons.Outlined.Settings,
        rounded = Icons.Rounded.Settings
    ),
    ARROW_BACK(
        filled = Icons.Filled.ArrowBack,
        outlined = Icons.Outlined.ArrowBack,
        rounded = Icons.Rounded.ArrowBack
    ),
    ARROW_FORWARD(
        filled = Icons.Filled.ArrowForward,
        outlined = Icons.Outlined.ArrowForward,
        rounded = Icons.Rounded.ArrowForward
    ),
    
    // Communication Icons
    SEND(
        filled = Icons.Filled.Send,
        outlined = Icons.Outlined.Send,
        rounded = Icons.Rounded.Send
    ),
    CHAT(
        filled = Icons.Filled.Chat,
        outlined = Icons.Outlined.Chat,
        rounded = Icons.Rounded.Chat
    ),
    EMAIL(
        filled = Icons.Filled.Email,
        outlined = Icons.Outlined.Email,
        rounded = Icons.Rounded.Email
    ),
    PHONE(
        filled = Icons.Filled.Phone,
        outlined = Icons.Outlined.Phone,
        rounded = Icons.Rounded.Phone
    ),
    
    // Action Icons
    ADD(
        filled = Icons.Filled.Add,
        outlined = Icons.Outlined.Add,
        rounded = Icons.Rounded.Add
    ),
    EDIT(
        filled = Icons.Filled.Edit,
        outlined = Icons.Outlined.Edit,
        rounded = Icons.Rounded.Edit
    ),
    DELETE(
        filled = Icons.Filled.Delete,
        outlined = Icons.Outlined.Delete,
        rounded = Icons.Rounded.Delete
    ),
    SAVE(
        filled = Icons.Filled.Save,
        outlined = Icons.Outlined.Save,
        rounded = Icons.Rounded.Save
    ),
    CLOSE(
        filled = Icons.Filled.Close,
        outlined = Icons.Outlined.Close,
        rounded = Icons.Rounded.Close
    ),
    
    // Status Icons
    CHECK(
        filled = Icons.Filled.Check,
        outlined = Icons.Outlined.Check,
        rounded = Icons.Rounded.Check
    ),
    ERROR(
        filled = Icons.Filled.Error,
        outlined = Icons.Outlined.Error,
        rounded = Icons.Rounded.Error
    ),
    WARNING(
        filled = Icons.Filled.Warning,
        outlined = Icons.Outlined.Warning,
        rounded = Icons.Rounded.Warning
    ),
    INFO(
        filled = Icons.Filled.Info,
        outlined = Icons.Outlined.Info,
        rounded = Icons.Rounded.Info
    ),
    
    // Content Icons
    ATTACH_FILE(
        filled = Icons.Filled.AttachFile,
        outlined = Icons.Outlined.AttachFile,
        rounded = Icons.Rounded.AttachFile
    ),
    IMAGE(
        filled = Icons.Filled.Image,
        outlined = Icons.Outlined.Image,
        rounded = Icons.Rounded.Image
    ),
    VIDEO_FILE(
        filled = Icons.Filled.VideoFile,
        outlined = Icons.Outlined.VideoFile,
        rounded = Icons.Rounded.VideoFile
    ),
    AUDIO_FILE(
        filled = Icons.Filled.AudioFile,
        outlined = Icons.Outlined.AudioFile,
        rounded = Icons.Rounded.AudioFile
    ),
    
    // Social Icons
    PERSON(
        filled = Icons.Filled.Person,
        outlined = Icons.Outlined.Person,
        rounded = Icons.Rounded.Person
    ),
    GROUP(
        filled = Icons.Filled.Group,
        outlined = Icons.Outlined.Group,
        rounded = Icons.Rounded.Group
    ),
    ACCOUNT_CIRCLE(
        filled = Icons.Filled.AccountCircle,
        outlined = Icons.Outlined.AccountCircle,
        rounded = Icons.Rounded.AccountCircle
    ),
    
    // Utility Icons
    MENU(
        filled = Icons.Filled.Menu,
        outlined = Icons.Outlined.Menu,
        rounded = Icons.Rounded.Menu
    ),
    MORE_VERT(
        filled = Icons.Filled.MoreVert,
        outlined = Icons.Outlined.MoreVert,
        rounded = Icons.Rounded.MoreVert
    ),
    REFRESH(
        filled = Icons.Filled.Refresh,
        outlined = Icons.Outlined.Refresh,
        rounded = Icons.Rounded.Refresh
    ),
    DOWNLOAD(
        filled = Icons.Filled.Download,
        outlined = Icons.Outlined.Download,
        rounded = Icons.Rounded.Download
    ),
    UPLOAD(
        filled = Icons.Filled.Upload,
        outlined = Icons.Outlined.Upload,
        rounded = Icons.Rounded.Upload
    ),
    
    // Business Icons
    BUSINESS(
        filled = Icons.Filled.Business,
        outlined = Icons.Outlined.Business,
        rounded = Icons.Rounded.Business
    ),
    WORK(
        filled = Icons.Filled.Work,
        outlined = Icons.Outlined.Work,
        rounded = Icons.Rounded.Work
    ),
    SCHOOL(
        filled = Icons.Filled.School,
        outlined = Icons.Outlined.School,
        rounded = Icons.Rounded.School
    ),
    
    // Time Icons
    SCHEDULE(
        filled = Icons.Filled.Schedule,
        outlined = Icons.Outlined.Schedule,
        rounded = Icons.Rounded.Schedule
    ),
    EVENT(
        filled = Icons.Filled.Event,
        outlined = Icons.Outlined.Event,
        rounded = Icons.Rounded.Event
    ),
    TODAY(
        filled = Icons.Filled.Today,
        outlined = Icons.Outlined.Today,
        rounded = Icons.Rounded.Today
    ),
    
    // Location Icons
    LOCATION_ON(
        filled = Icons.Filled.LocationOn,
        outlined = Icons.Outlined.LocationOn,
        rounded = Icons.Rounded.LocationOn
    ),
    MAP(
        filled = Icons.Filled.Map,
        outlined = Icons.Outlined.Map,
        rounded = Icons.Rounded.Map
    ),
    PLACE(
        filled = Icons.Filled.Place,
        outlined = Icons.Outlined.Place,
        rounded = Icons.Rounded.Place
    )
}
