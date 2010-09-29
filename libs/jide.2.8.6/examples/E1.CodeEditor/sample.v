/*----------------------- PREP OPTIMIZED DESIGN REPORT -------------------
-- FILE NAME             : cntrl.v
-- DESIGN NAME           : CONTROL STATE MACHINE FOR R4000
-- DESIGN DESCRIPTION    : MIPS R4000 Processor
-- LANGUAGE              : Verilog
-- SYNTHESIS PRODUCT/REV : Synplify Lite 2.1c
-- SYNTHESIS VENDOR      : Synplicity, Inc.
-- SIMULATION PRODUCT/REV: SILOS III V95.1
-- SIMULATION VENDOR     : SIMUCAD, Inc
-- APR PRODUCT/REV       : SpDE 5.04
-- APR PRODUCT/REV       : QuickLogic QuickWorks 5.04
-- AUTHOR NAME           : Joel Naumann, Amarpreet Chawla, Micheal Povlin
-- AUTHOR COMPANY        : IDT, Stratacom, QuickLogic
-- AUTHOR EMAIL          : jcn@strata.com, povlin@idtinc.com, amar@qlogic.com
-- COMMENTS              :
-- DATE                  : 7/21/94
------------------------------------------------------------------------*/
/*********************************************************************
*
*               Module Name:    cntrl.v
*               Purpose:        State Machine for control signals in R4000
*               Copyright:      Michael Povlin, Amarpreet S. Chawla
*                               Joel Naumann
*
* Please feel free to distribute as long as the header is attached.  
*        
*********************************************************************/

`define LW 6'b100011
`define SW 6'b101011
`define RTYPE 6'b000000
`define BRANCH 6'b000100
`define JUMP 6'b000010

module mCntrl (

/* input */ 
        Op,
        Clk,
        Reset,
/* output */
        PCWrite,
        PCWriteCond,
        IorD,
        MemRead,
        MemWrite,
        IRWrite,
        MemtoReg,
        PCSource,
        TargetWrite,
        ALUOp,
        ALUSelA,
        ALUSelB,
        RegWrite,
        RegDst);

input [5:0] Op;
input Clk, Reset;

output PCWrite, PCWriteCond, IorD, MemRead, MemWrite, IRWrite, MemtoReg;
output [1:0] PCSource, ALUOp, ALUSelB;
output ALUSelA, TargetWrite, RegWrite, RegDst;


/* One hot encoding */
parameter 
        pInstFetch  = 11'b00000000001, pTestInstFetch  = 11'bxxxxxxxxxx1,
        pInstDcode  = 11'b00000000010, pTestInstDcode  = 11'bxxxxxxxxx1x,
        pMemAddr    = 11'b00000000100, pTestMemAddr    = 11'bxxxxxxxx1xx,
        pLwMemAcss  = 11'b00000001000, pTestLwMemAcss  = 11'bxxxxxxx1xxx,
        pWrtBack    = 11'b00000010000, pTestWrtBack    = 11'bxxxxxx1xxxx,
        pSwMemAcss  = 11'b00000100000, pTestSwMemAcss  = 11'bxxxxx1xxxxx,
        pRTypeExe   = 11'b00001000000, pTestRTypeExe   = 11'bxxxx1xxxxxx,
        pRTypeComp  = 11'b00010000000, pTestRTypeComp  = 11'bxxx1xxxxxxx,
        pBranch     = 11'b00100000000, pTestBranch     = 11'bxx1xxxxxxxx,
        pBranchComp = 11'b01000000000, pTestBranchComp = 11'bx1xxxxxxxxx,
        pJumpComp   = 11'b10000000000, pTestJumpComp   = 11'b1xxxxxxxxxx,
        pSx 	      = 11'bxxxxxxxxxxx;
     
wire [5:0] Op;
wire Clk, Reset;
reg PCWrite, PCWriteCond, IorD, MemRead, MemWrite, IRWrite, MemtoReg;
reg [1:0] PCSource, ALUOp, ALUSelB;
reg ALUSelA, TargetWrite, RegWrite, RegDst;
reg [10:0] state;

always @(posedge Clk )
begin
   if (Reset) begin
      {PCWrite, PCWriteCond, IorD, MemRead, 
       MemWrite, IRWrite, MemtoReg}                  = 7'b0001010;
      {PCSource, ALUOp, ALUSelB}                     = 6'b000001;
      {ALUSelA, TargetWrite, RegWrite, RegDst}       = 4'b0000;
      state = pInstFetch;
      end
   else begin
      casex (state)
         pTestInstFetch: begin
            {PCWrite, PCWriteCond, IorD, MemRead, 
             MemWrite, IRWrite, MemtoReg}            = 7'b1000000;
            {PCSource, ALUOp, ALUSelB}               = 6'b000001;
            {ALUSelA, TargetWrite, RegWrite, RegDst} = 4'b0100;
            state =  pInstDcode;
         end

         pTestInstDcode: begin 
            {PCWrite, PCWriteCond, IorD, MemRead, 
             MemWrite, IRWrite, MemtoReg}            = 7'b0010000;
            {PCSource, ALUOp, ALUSelB}               = 6'b000011;
            {ALUSelA, TargetWrite, RegWrite, RegDst} = 4'b1100;

            if (Op == `LW || Op == `SW) begin
                ALUSelA =  1'b0;
                state =  pMemAddr;
                end
            else if (Op == `RTYPE) begin
                state =  pRTypeExe;
                end 
            else if (Op == `BRANCH) begin
                state =  pBranch;
                ALUSelA =  1'b0;
                end
            else if (Op == `JUMP) begin
                PCWrite =  1'b1;
                PCSource =  2'b10;
                state =  pJumpComp;
                end
            end

         pTestMemAddr: begin 
            {PCWrite, PCWriteCond, IorD, MemRead,
             MemWrite, IRWrite, MemtoReg}            =  7'b0000001;
            {PCSource, ALUOp, ALUSelB}               =  6'b000011;
            {ALUSelA, TargetWrite, RegWrite, RegDst} =  4'b1000;

            if (Op == `LW) begin
               MemRead =  1'b1;
               IorD =  1'b1;
               RegWrite =  1'b1;
               state =  pLwMemAcss;
               end
            else begin
               state =  pSwMemAcss;
               MemWrite =  1'b1;
               IorD =  1'b1;
               end
            end

         pTestLwMemAcss: begin 
            {PCWrite, PCWriteCond, IorD, MemRead,
             MemWrite, IRWrite, MemtoReg}            =  7'b0000000;
            {PCSource, ALUOp, ALUSelB}               =  6'b000001;
            {ALUSelA, TargetWrite, RegWrite, RegDst} =  4'b0000;

            state =  pWrtBack;
            end

         pTestWrtBack:	begin
            {PCWrite, PCWriteCond, IorD, MemRead, 
             MemWrite, IRWrite, MemtoReg}            =  7'b0001010;
            {PCSource, ALUOp, ALUSelB}               =  6'b000001;
            {ALUSelA, TargetWrite, RegWrite, RegDst} =  4'b0000;
            state =  pInstFetch;
            end

         pTestSwMemAcss: begin
            {PCWrite, PCWriteCond, IorD, MemRead, 
             MemWrite, IRWrite, MemtoReg}            =  7'b0001010;
            {PCSource, ALUOp, ALUSelB}               =  6'b000001;
            {ALUSelA, TargetWrite, RegWrite, RegDst} =  4'b0000;
            state =  pInstFetch;
            end

         pTestRTypeExe: begin
            {PCWrite, PCWriteCond, IorD, MemRead, 
             MemWrite, IRWrite, MemtoReg}            =  7'b0000000;
            {PCSource, ALUOp, ALUSelB}               =  6'b001000;
            {ALUSelA, TargetWrite, RegWrite, RegDst} =  4'b1011;
            state =  pRTypeComp;
            end

         pTestRTypeComp: begin
            {PCWrite, PCWriteCond, IorD, MemRead,
             MemWrite, IRWrite, MemtoReg}            =  7'b0001010;
            {PCSource, ALUOp, ALUSelB}               =  6'b000000;
            {ALUSelA, TargetWrite, RegWrite, RegDst} =  4'b1001;
            state =  pInstFetch;
            end

         pTestBranch: begin
            {PCWrite, PCWriteCond, IorD, MemRead, 
             MemWrite, IRWrite, MemtoReg}            =  7'b0100000;
            {PCSource, ALUOp, ALUSelB}               =  6'b010100;
            {ALUSelA, TargetWrite, RegWrite, RegDst} =  4'b1000;
            state =  pBranchComp;
            end

         pTestBranchComp: begin
            {PCWrite, PCWriteCond, IorD, MemRead, 
             MemWrite, IRWrite, MemtoReg}            =  7'b0001010;
            {PCSource, ALUOp, ALUSelB}               =  6'b010100;
            {ALUSelA, TargetWrite, RegWrite, RegDst} =  4'b1000;
            state =  pInstFetch;
            end

         pTestJumpComp: begin
            {PCWrite, PCWriteCond, IorD, MemRead, 
             MemWrite, IRWrite, MemtoReg}            =  7'b0001010;
            {PCSource, ALUOp, ALUSelB}               =  6'b100000;
            {ALUSelA, TargetWrite, RegWrite, RegDst} =  4'b0000;
            state =  pInstFetch;
            end

         default: begin
            {PCWrite, PCWriteCond, IorD, MemRead, 
             MemWrite, IRWrite, MemtoReg}            =  7'bxxxxxxx;
            {PCSource, ALUOp, ALUSelB}               =  6'bxxxxxx;
            {ALUSelA, TargetWrite, RegWrite, RegDst} =  4'bxxxx;
            state =  pSx;
            $display ("Warning: Unknown State");
            end
      endcase
   end /*else*/
end  /*always*/
endmodule /*cntrl*/
 
