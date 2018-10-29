// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed.
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.
//
// - Keyboard RAM address: RAM[24576]
// - Screen RAM address: 16384
// - @SCREEN: address of pixel at top left corner
// Screen: 512x256 (256 rows, 512 columns laid out as 32 16-bit registers)
// Access logic for pixel at row r and column c
// => Word access: RAM[16384 + rx32 + c/16]
// 1. Compute the address for the row (0..255)
// 2. For i=1..15, process pixel at i

    @i        // Variable to track pixel index
    M=0
(LOOP)
    @24576    // Load address of keyboard memory
    D=M
    @WHITEN
    D;JEQ     // If D=0 (no keystroke), goto ClearScreen
    @BLACKEN
    D;JGT     // IF D>0 (keystroke detected), goto BLACKEN
    @LOOP
    0;JMP     // Infinite loop
(WHITEN)
    @SCREEN
    D=M
    @i
    M=D+M
    // M=D
    D=M
    M=1       // Light the pixel!
    @i
    M=M+1
    @LOOP
    0;JMP     // Goto Loop

(BLACKEN)
    @SCREEN
    //D=M
    @i
    M=D+M
    //M=D
    D=M
    M=1       // Light the pixel!
    @i
    M=M+1
    @LOOP
    0;JMP     // Goto Loop
(END)

/*
(START)
  @SCREEN
  D=A
  @pixel // use pixel as a variable that holds the address of the current pixel
  M=D // initialize it to the top left pixel of the screen

(LOOP)
  @KBD // keyboard input
  D=M
  @WHITE
  D;JEQ
  @BLACK
  0;JMP

(WHITE)
  @pixel
  //A=M // set current pixel address to A
  M=0 // white it out
  @NEXT
  0;JMP

(BLACK)
  @pixel
  //A=M // set current pixel address to A
  M=-1 // black it out
  @NEXT
  0;JMP

(NEXT)
  @pixel
  D=M+1
  M=D // set pixel to pixel + 1
  @KBD
  D=A-D
  @START
  D;JEQ
  @LOOP
  0;JMP
  */