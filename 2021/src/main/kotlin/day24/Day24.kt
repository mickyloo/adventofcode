package day24

import java.io.File
import kotlin.collections.ArrayDeque
import kotlin.system.measureTimeMillis

fun main() {
    val instructions = File("src/main/kotlin/day24/input.example.txt")
        .readLines()
        .map {
            val parts = it.split(" ")
            val op = Op.valueOf(parts[0].uppercase())
            val register = Register.valueOf(parts[1])
            when(op) {
                Op.INP -> Instr(op, register, null)
                else -> Instr(op, register, parts[2])
            }
        }

    val alu = ALU(instructions)

    val elapsed1 = measureTimeMillis { part1(alu) }
    //val elapsed2 = measureTimeMillis { part1(alu) }

    println("Part1: Took $elapsed1 ms")
    //println("Part2: Took $elapsed2 ms")
}

enum class Op {
    INP, ADD, MUL, DIV, MOD, EQL
}

enum class Register() {
    w, x, y, z
}

typealias Instr = Triple<Op, Register, String?>
class ALU(private val program: List<Instr>) {

    private val registerNames = Register.values().map { it.name }.toSet()

    fun execute(input: ArrayDeque<Int>) {
        val registers = Register.values().associateWith { 0 }.toMutableMap()
        for(instr in program) {
            val operand = when (instr.third) {
                null -> input.removeFirst()
                in registerNames -> registers.getOrDefault(Register.valueOf(instr.third!!), 0)
                else -> instr.third!!.toInt()
            }

            registers[instr.second] = when(instr.first) {
                Op.INP -> operand
                Op.ADD -> registers.getOrDefault(instr.second, 0) + operand
                Op.MUL -> registers.getOrDefault(instr.second, 0) * operand
                Op.DIV -> registers.getOrDefault(instr.second, 0) / operand
                Op.MOD -> registers.getOrDefault(instr.second, 0).mod(operand)
                Op.EQL -> if (registers.getOrDefault(instr.second, 0) == operand) 1 else 0
            }
        }

        println(registers)
    }
}

fun part1(alu: ALU) {
    val input = ArrayDeque<Int>()
    input.add(14)
    alu.execute(input)
}
