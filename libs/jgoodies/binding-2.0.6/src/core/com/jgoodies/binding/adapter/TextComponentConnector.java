/*
 * Copyright (c) 2002-2008 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.jgoodies.binding.adapter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

import com.jgoodies.binding.BindingUtils;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;

/**
 * Connects a String typed ValueModel and a JTextField or JTextArea.
 * At construction time the text component content is updated
 * with the subject's contents.<p>
 *
 * This connector has been designed for text components that display a plain
 * String. In case of a JEditorPane, the binding may require more information
 * then the plain String, for example styles. Since this is outside the scope
 * of this connector, the public constructors prevent a construction for
 * general JTextComponents. If you want to establish a one-way binding for
 * a display JEditorPane, use a custom listener instead.<p>
 *
 * This class provides limited support for handling
 * subject value modifications while updating the subject.
 * If a Document change initiates a subject value update, the subject
 * will be observed and a property change fired by the subject will be
 * handled - if any. In most cases, the subject will notify about a
 * change to the text that was just set by this connector.
 * However, in some cases the subject may decide to modify this text,
 * for example to ensure upper case characters.
 * Since at this moment, this adapter's Document is still write-locked,
 * the Document update is performed later using
 * <code>SwingUtilities#invokeLater</code>.<p>
 *
 * <strong>Note:</strong>
 * Such an update will typically change the Caret position in JTextField's
 * and other JTextComponent's that are synchronized using this class.
 * Hence, the subject value modifications can be used with
 * commit-on-focus-lost text components, but typically not with a
 * commit-on-key-typed component. For the latter case, you may consider
 * using a custom <code>DocumentFilter</code>.<p>
 *
 * <strong>Constraints:</strong>
 * The ValueModel must be of type <code>String</code>.<p>
 *
 * <strong>Examples:</strong><pre>
 * ValueModel lastNameModel = new PropertyAdapter(customer, "lastName", true);
 * JTextField lastNameField = new JTextField();
 * TextComponentConnector.connect(lastNameModel, lastNameField);
 *
 * ValueModel codeModel = new PropertyAdapter(shipment, "code", true);
 * JTextField codeField = new JTextField();
 * TextComponentConnector connector =
 *     new TextComponentConnector(codeModel, codeField);
 * connector.updateTextComponent();
 * </pre>
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.12 $
 *
 * @see     ValueModel
 * @see     Document
 * @see     PlainDocument
 *
 * @since 1.2
 */
public final class TextComponentConnector {

    /**
     * Holds the underlying ValueModel that is used to read values,
     * to update the document and to write values if the document changes.
     */
    private final ValueModel subject;

    /**
     * Refers to the text component that shall be synchronized
     * with the subject.
     */
    private final JTextComponent textComponent;

    /**
     * Holds the text component's current document.
     * Used for the rare case where the text component fires
     * a PropertyChangeEvent for the "document" property
     * with oldValue or newValue == {@code null}.
     */
    private Document document;

    private final SubjectValueChangeHandler subjectValueChangeHandler;

    private final DocumentListener textChangeHandler;

    private final PropertyChangeListener documentChangeHandler;


    // Instance Creation ******************************************************

    /**
     * Constructs a TextComponentConnector that connects the specified
     * String-typed subject ValueModel with the given text area.<p>
     *
     * In case you don't need the TextComponentConnector instance, you better
     * use one of the static <code>#connect</code> methods.
     * This constructor may confuse developers, if you just use
     * the side effects performed in the constructor; this is because it is
     * quite unconventional to instantiate an object that you never use.
     *
     * @param subject    the underlying String typed ValueModel
     * @param textArea   the JTextArea to be synchronized with the ValueModel
     *
     * @throws NullPointerException  if the subject or text area is {@code null}
     */
    public TextComponentConnector(ValueModel subject, JTextArea textArea) {
        this(subject, (JTextComponent) textArea);
    }

    /**
     * Constructs a TextComponentConnector that connects the specified
     * String-typed subject ValueModel with the given text field.<p>
     *
     * In case you don't need the TextComponentConnector instance, you better
     * use one of the static <code>#connect</code> methods.
     * This constructor may confuse developers, if you just use
     * the side effects performed in the constructor; this is because it is
     * quite unconventional to instantiate an object that you never use.
     *
     * @param subject     the underlying String typed ValueModel
     * @param textField   the JTextField to be synchronized with the ValueModel
     *
     * @throws NullPointerException  if the subject or text field is {@code null}
     */
    public TextComponentConnector(ValueModel subject, JTextField textField) {
        this(subject, (JTextComponent) textField);
    }

    /**
     * Constructs a TextComponentConnector that connects the specified
     * String-typed subject ValueModel with the given JTextComponent.<p>
     *
     * In case you don't need the TextComponentConnector instance, you better
     * use one of the static <code>#connect</code> methods.
     * This constructor may confuse developers, if you just use
     * the side effects performed in the constructor; this is because it is
     * quite unconventional to instantiate an object that you never use.
     *
     * @param subject       the underlying String typed ValueModel
     * @param textComponent the JTextComponent to be synchronized with the ValueModel
     *
     * @throws NullPointerException  if the subject or text component is {@code null}
     */
    private TextComponentConnector(ValueModel subject, JTextComponent textComponent) {
        if (subject == null)
            throw new NullPointerException("The subject must not be null.");
        if (textComponent == null)
            throw new NullPointerException("The text component must not be null.");
        this.subject = subject;
        this.textComponent = textComponent;
        this.subjectValueChangeHandler = new SubjectValueChangeHandler();
        this.textChangeHandler = new TextChangeHandler();
        document = textComponent.getDocument();
        reregisterTextChangeHandler(null, document);
        subject.addValueChangeListener(subjectValueChangeHandler);
        documentChangeHandler = new DocumentChangeHandler();
        textComponent.addPropertyChangeListener("document", documentChangeHandler);
    }


    /**
     * Establishes a synchronization between the specified String-typed
     * subject ValueModel and the given text area. Does not synchronize now.
     *
     * @param subject    the underlying String typed ValueModel
     * @param textArea   the JTextArea to be synchronized with the ValueModel
     *
     * @throws NullPointerException  if the subject or text area is {@code null}
     */
    public static void connect(ValueModel subject, JTextArea textArea) {
        new TextComponentConnector(subject, textArea);
    }


    /**
     * Establishes a synchronization between the specified String-typed
     * subject ValueModel and the given text field. Does not synchronize now.
     *
     * @param subject    the underlying String typed ValueModel
     * @param textField   the JTextField to be synchronized with the ValueModel
     *
     * @throws NullPointerException  if the subject or text area is {@code null}
     */
    public static void connect(ValueModel subject, JTextField textField) {
        new TextComponentConnector(subject, textField);
    }


    // Synchronization ********************************************************

    /**
     * Reads the current text from the document
     * and sets it as new value of the subject.
     */
    public void updateSubject() {
        setSubjectText(getDocumentText());
    }


    public void updateTextComponent() {
        setDocumentTextSilently(getSubjectText());
    }

    /**
     * Returns the text contained in the document.
     *
     * @return the text contained in the document
     */
    private String getDocumentText() {
        return textComponent.getText();
    }

    /**
     * Sets the text component's contents without notifying the subject
     * about the change. Invoked by the subject change listener.
     * Sets the text, then sets the caret position to 0.
     *
     * @param newText  the text to be set in the document
     */
    private void setDocumentTextSilently(String newText) {
        textComponent.getDocument().removeDocumentListener(textChangeHandler);
        textComponent.setText(newText);
        textComponent.setCaretPosition(0);
        textComponent.getDocument().addDocumentListener(textChangeHandler);
    }


    /**
     * Returns the subject's text value.
     *
     * @return the subject's text value
     * @throws ClassCastException   if the subject value is not a String
     */
    private String getSubjectText() {
        String str = (String) subject.getValue();
        return str == null ? "" : str;
    }

    /**
     * Sets the given text as new subject value. Since the subject may modify
     * this text, we cannot update silently, i.e. we cannot remove and add
     * the subjectValueChangeHandler before/after the update. Since this
     * change is invoked during a Document write operation, the document
     * is write-locked and so, we cannot modify the document before all
     * document listeners have been notified about the change.<p>
     *
     * Therefore we listen to subject changes and defer any document changes
     * using <code>SwingUtilities.invokeLater</code>. This mode is activated
     * by setting the subject change handler's <code>updateLater</code> to true.
     *
     * @param newText   the text to be set in the subject
     */
    private void setSubjectText(String newText) {
        subjectValueChangeHandler.setUpdateLater(true);
        try {
            subject.setValue(newText);
        } finally {
            subjectValueChangeHandler.setUpdateLater(false);
        }
    }

    private void reregisterTextChangeHandler(Document oldDocument, Document newDocument) {
        if (oldDocument != null) {
            oldDocument.removeDocumentListener(textChangeHandler);
        }
        if (newDocument != null) {
            newDocument.addDocumentListener(textChangeHandler);
        }
    }


    // Misc *******************************************************************

    /**
     * Removes the internal listeners from the subject, text component,
     * and text component's document.
     * This connector must not be used after calling <code>#release</code>.<p>
     *
     * To avoid memory leaks it is recommended to invoke this method,
     * if the ValueModel lives much longer than the text component.
     * Instead of releasing a text connector, you typically make the ValueModel
     * obsolete by releasing the PresentationModel or BeanAdapter that has
     * created the ValueModel.<p>
     *
     * As an alternative you may use ValueModels that in turn use
     * event listener lists implemented using <code>WeakReference</code>.
     *
     * @see PresentationModel#release()
     * @see BeanAdapter#release()
     * @see java.lang.ref.WeakReference
     */
    public void release() {
        reregisterTextChangeHandler(document, null);
        subject.removeValueChangeListener(subjectValueChangeHandler);
        textComponent.removePropertyChangeListener("document", documentChangeHandler);
    }


    // DocumentListener *******************************************************

    /**
     * Updates the subject if the text has changed.
     */
    private final class TextChangeHandler implements DocumentListener {

        /**
         * There was an insert into the document; update the subject.
         *
         * @param e the document event
         */
        public void insertUpdate(DocumentEvent e) {
            updateSubject();
        }

        /**
         * A portion of the document has been removed; update the subject.
         *
         * @param e the document event
         */
        public void removeUpdate(DocumentEvent e) {
            updateSubject();
        }

        /**
         * An attribute or set of attributes has changed; do nothing.
         *
         * @param e the document event
         */
        public void changedUpdate(DocumentEvent e) {
            // Do nothing on attribute changes.
        }
    }


    /**
     * Handles changes in the subject value and updates this document
     * - if necessary.<p>
     *
     * Document changes update the subject text and result in a subject
     * property change. Most of these changes will just reflect the
     * former subject change. However, in some cases the subject may
     * modify the text set, for example to ensure upper case characters.
     * This method reduces the number of document updates by checking
     * the old and new text. If the old and new text are equal or
     * both null, this method does nothing.<p>
     *
     * Since subject changes as a result of a document change may not
     * modify the write-locked document immediately, we defer the update
     * if necessary using <code>SwingUtilities.invokeLater</code>.<p>
     *
     * See the TextComponentConnector's JavaDoc class comment
     * for the limitations of the deferred document change.
     */
    private final class SubjectValueChangeHandler implements PropertyChangeListener {

        private boolean updateLater;

        void setUpdateLater(boolean updateLater) {
            this.updateLater = updateLater;
        }

        /**
         * The subject value has changed; updates the document immediately
         * or later - depending on the <code>updateLater</code> state.
         *
         * @param evt   the event to handle
         */
        public void propertyChange(PropertyChangeEvent evt) {
            final String oldText = getDocumentText();
            final Object newValue = evt.getNewValue();
            final String newText = newValue == null
                ? getSubjectText()
                : (String) newValue;
            if (BindingUtils.equals(oldText, newText))
                return;

            if (updateLater) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        setDocumentTextSilently(newText);
                    }
                });
            } else {
                setDocumentTextSilently(newText);
            }
        }

    }


    /**
     * Re-registers the text change handler after document changes.
     */
    private final class DocumentChangeHandler implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            Document oldDocument = document;
            Document newDocument = textComponent.getDocument();
            reregisterTextChangeHandler(oldDocument, newDocument);
            document = newDocument;
        }
    }


}
