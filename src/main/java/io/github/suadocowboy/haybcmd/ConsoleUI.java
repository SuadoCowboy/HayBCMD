/*
 * MIT License
 *
 * Copyright (c) 2024 Lucca Rieffel Silva, also as Suado Cowboy
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the “Software”),
 * to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.github.suadocowboy.haybcmd;

import io.github.suadocowboy.haybcmd.compatibility.Output;
import io.github.suadocowboy.haybcmd.parser.Parser;

import javax.swing.*;
import java.awt.*;
import java.util.Dictionary;
import java.util.Objects;

public class ConsoleUI extends JFrame {
    private final JTextArea outputTextArea;
    private final JTextField inputTextField;

    private void addTextToOutputTextArea(String message) {
        outputTextArea.setText(outputTextArea.getText() + message);
    }

    private void addTextToOutputTextAreaWithNewLine(String message) {
        outputTextArea.setText(outputTextArea.getText() + message + "\n");
    }

    public ConsoleUI(Dictionary<String, String> variables) {
        setTitle("Hay-B Console");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setFocusable(true);

        Color darkGray = new Color(30, 30, 30);
        getContentPane().setBackground(darkGray);

        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);
        outputTextArea.setForeground(Color.WHITE); // Set text color
        outputTextArea.setBackground(darkGray); // Set background color
        outputTextArea.setBorder(null);
        outputTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Set font size
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // Show scrollbar only when needed
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // wow those functions names are big haha...
        Output.init(this::addTextToOutputTextArea, this::addTextToOutputTextAreaWithNewLine);

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            Output.println("ERROR: Could not set look and feel");
        }

        inputTextField = new JTextField();
        inputTextField.setForeground(Color.WHITE); // Set text color
        inputTextField.setBackground(new Color(20, 20, 20)); // Set background color
        inputTextField.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Set font size
        inputTextField.setBorder(null);

        inputTextField.addActionListener(e -> {
            String input = inputTextField.getText();
            inputTextField.setText("");

            Output.println(input);

            if (Objects.equals(input, "quit")) {
                dispose();
                return;
            }

            new Parser(new Lexer(input), variables).parse();
        });

        add(inputTextField, BorderLayout.SOUTH);

        pack();
        setSize(500, 500);
        setLocationRelativeTo(null); // Center the window
    }

}
