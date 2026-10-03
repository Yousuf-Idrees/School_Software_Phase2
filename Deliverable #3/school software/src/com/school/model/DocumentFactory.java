package com.school.model;

/**
 * Document Creation System
 *
 * Manages creation of documents in different formats.
 */
 * - Easy to switch format
 * - Clean structure
 *
 * Classes:
 * - DocumentFactory
 * - PDFDocumentFactory
 * - WordDocumentFactory
 * - DocumentHeader
 * - DocumentBody
 * - DocumentFooter
 */

// 1. Interfaces
interface DocumentHeader { void render(); }
interface DocumentBody { void render(); }
interface DocumentFooter { void render(); }

public interface DocumentFactory {
    DocumentHeader createHeader();
    DocumentBody createBody();
    DocumentFooter createFooter();
}

// 2. PDF Implementation
class PDFHeader implements DocumentHeader {
    public void render() { System.out.println("PDF Header: School Report"); }
}
class PDFBody implements DocumentBody {
    public void render() { System.out.println("PDF Body: Student grades and attendance"); }
}
class PDFFooter implements DocumentFooter {
    public void render() { System.out.println("PDF Footer: Generated on " + java.time.LocalDate.now()); }
}

class PDFDocumentFactory implements DocumentFactory {
    public DocumentHeader createHeader() { return new PDFHeader(); }
    public DocumentBody createBody() { return new PDFBody(); }
    public DocumentFooter createFooter() { return new PDFFooter(); }
}

// 3. Word Implementation
class WordHeader implements DocumentHeader {
    public void render() { System.out.println("Word Header: School Report"); }
}
class WordBody implements DocumentBody {
    public void render() { System.out.println("Word Body: Student grades and attendance"); }
}
class WordFooter implements DocumentFooter {
    public void render() { System.out.println("Word Footer: Generated on " + java.time.LocalDate.now()); }
}

class WordDocumentFactory implements DocumentFactory {
    public DocumentHeader createHeader() { return new WordHeader(); }
    public DocumentBody createBody() { return new WordBody(); }
    public DocumentFooter createFooter() { return new WordFooter(); }
}