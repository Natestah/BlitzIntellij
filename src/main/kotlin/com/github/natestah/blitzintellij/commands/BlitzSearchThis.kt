package com.github.natestah.blitzintellij.commands

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent


import com.intellij.lang.Language;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.impl.status.StatusBarUtil;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.sun.source.tree.NewArrayTree

import java.util.ArrayList
import java.io.FileWriter
import java.io.BufferedWriter
import java.io.File
import java.lang.Object



class BlitzSearchThis : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {

        // Get required data keys
        val editor = event.getData(PlatformDataKeys.EDITOR) ?: return
        val project: Project = event.getData(PlatformDataKeys.PROJECT) ?: return
        val document: Document = editor.getDocument()

        val selectText = getSearchQueryFromEditor(editor);

        // Retrieve the value of the OS environment variable
        val appdataEnv: String = java.lang.System.getenv("appdata")
        var directory: String = appdataEnv + "\\NathanSilvers\\POORMANS_IPC"


        val newDirectory: File = File(directory)
        if( !newDirectory.exists() )
        {
            newDirectory.mkdir();
        }
        
        var path = directory + "\\SET_SEARCH.txt";
        val writer: BufferedWriter = BufferedWriter(FileWriter(path));
        writer.write(selectText);
        writer.close();

        val programFiles: String = java.lang.System.getenv("programfiles")
        val blitzPath: String = programFiles + "\\Blitz\\Blitz.exe";
        val blitsFile: File = File(blitzPath);
        if( blitsFile.exists() )
        {
            val process: java.lang.Process = java.lang.ProcessBuilder(blitzPath).start()
        }
        else 
        {
            BrowserUtil.browse( "https://natestah.com/download")
        }
            
        
    }

    private fun getSearchQueryFromEditor(editor: Editor): String {
        val selectionModel: SelectionModel = editor.getSelectionModel()
        if (selectionModel.hasSelection()) {
            return selectionModel.getSelectedText() ?: "";
        }

        return "@" + getWordAtCursor(editor)
    }
    
    private fun getWordAtCursor(editor: Editor): String? {
        val editorText: CharSequence = editor.getDocument().getCharsSequence()
        var cursorOffset: Int = editor.getCaretModel().getOffset()
        val editorTextLength = editorText.length

        if (editorTextLength == 0) {
            return null
        }

        if ((cursorOffset >= editorTextLength) || (cursorOffset > 1 && !isIdentifierPart(
                editorText[cursorOffset]
            ) && isIdentifierPart(editorText[cursorOffset - 1]))
        ) {
            cursorOffset--
        }

        if (isIdentifierPart(editorText[cursorOffset])) {
            var start = cursorOffset
            var end = cursorOffset

            while (start > 0 && isIdentifierPart(editorText[start - 1])) {
                start--
            }

            while (end < editorTextLength && isIdentifierPart(editorText[end])) {
                end++
            }

            return editorText.subSequence(start, end).toString()
        }
        return null
    }
    
    private fun isIdentifierPart(ch: Char): Boolean {
        return java.lang.Character.isJavaIdentifierPart(ch)
    }

}