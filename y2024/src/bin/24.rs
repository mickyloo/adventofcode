use adv_code_2024::*;
use anyhow::*;
use code_timing_macros::time_snippet;
use const_format::concatcp;
use itertools::Itertools;
use std::collections::HashMap;
use std::fs::File;
use std::io::{BufRead, BufReader};

const DAY: &str = "24"; // TODO: Fill the day
const INPUT_FILE: &str = concatcp!("input/", DAY, ".txt");

const TEST: &str = "\
x00: 1
x01: 1
x02: 1
y00: 0
y01: 1
y02: 0

x00 AND y00 -> z00
x01 XOR y01 -> z01
x02 OR y02 -> z02";

const TEST2: &str = "\
x00: 1
x01: 0
x02: 1
x03: 1
x04: 0
y00: 1
y01: 1
y02: 1
y03: 1
y04: 1

ntg XOR fgs -> mjb
y02 OR x01 -> tnw
kwq OR kpj -> z05
x00 OR x03 -> fst
tgd XOR rvg -> z01
vdt OR tnw -> bfw
bfw AND frj -> z10
ffh OR nrd -> bqk
y00 AND y03 -> djm
y03 OR y00 -> psh
bqk OR frj -> z08
tnw OR fst -> frj
gnj AND tgd -> z11
bfw XOR mjb -> z00
x03 OR x00 -> vdt
gnj AND wpb -> z02
x04 AND y00 -> kjc
djm OR pbm -> qhw
nrd AND vdt -> hwm
kjc AND fst -> rvg
y04 OR y02 -> fgs
y01 AND x02 -> pbm
ntg OR kjc -> kwq
psh XOR fgs -> tgd
qhw XOR tgd -> z09
pbm OR djm -> kpj
x03 XOR y03 -> ffh
x00 XOR y04 -> ntg
bfw OR bqk -> z06
nrd XOR fgs -> wpb
frj XOR qhw -> z04
bqk OR frj -> z07
y03 OR x01 -> nrd
hwm AND bqk -> z03
tgd XOR rvg -> z12
tnw OR pbm -> gnj";

fn main() -> Result<()> {
    start_day(DAY);

    //region Part 1
    println!("=== Part 1 ===");

    fn part1<R: BufRead>(reader: R) -> Result<usize> {
        // TODO: Solve Part 1 of the puzzle
        let mut registers: HashMap<String, usize> = HashMap::new();
        let mut instructions: HashMap<String, String> = HashMap::new();

        reader.lines().for_each(|line| {
            let line = line.unwrap();
            if line.contains("->") {
                let (equation, result) = line.split_once(" -> ").unwrap();
                instructions.insert(result.parse().unwrap(), equation.parse().unwrap());
            } else if line.len() > 0 {
                let (key, value) = line.split_once(": ").unwrap();
                let value = value.parse::<usize>().unwrap();
                &registers.insert(key.to_string(), value);
            }
        });

        fn eval(
            instructions: &HashMap<String, String>,
            registers: &mut HashMap<String, usize>,
            key: String,
        ) -> usize {
            let equation = instructions.get(key.as_str()).unwrap();
            let (left, op, right) = equation.splitn(3, ' ').collect_tuple().unwrap();
            let left_val: usize = if !registers.contains_key(&left.to_string()) {
                eval(instructions, registers, left.to_string())
            } else {
                *(registers.get(&left.to_string()).unwrap())
            };

            let right_val: usize = if !registers.contains_key(&right.to_string()) {
                eval(instructions, registers, right.to_string())
            } else {
                *(registers.get(&right.to_string()).unwrap())
            };

            let value = match op {
                "XOR" => left_val ^ right_val,
                "AND" => left_val & right_val,
                "OR" => left_val | right_val,
                _ => 0,
            };

            registers.insert(key, value);
            value
        }

        instructions
            .iter()
            .filter(|&e| e.0.starts_with("z"))
            .for_each(|e| {
                eval(&instructions, &mut registers, e.0.to_string());
            });

        let answer = registers
            .iter()
            .filter(|&e| e.0.starts_with("z"))
            .map(|e| {
                let bit = e.0[1..].parse::<usize>().unwrap();
                e.1 << bit
            })
            .sum();

        Ok(answer)
    }

    // TODO: Set the expected answer for the test input
    assert_eq!(4, part1(BufReader::new(TEST.as_bytes()))?);
    assert_eq!(2024, part1(BufReader::new(TEST2.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part1(input_file)?);
    println!("Result = {}", result);
    //endregion

    //region Part 2
    // println!("\n=== Part 2 ===");
    //
    // fn part2<R: BufRead>(reader: R) -> Result<usize> {
    //     Ok(0)
    // }
    //
    // assert_eq!(0, part2(BufReader::new(TEST.as_bytes()))?);
    //
    // let input_file = BufReader::new(File::open(INPUT_FILE)?);
    // let result = time_snippet!(part2(input_file)?);
    // println!("Result = {}", result);
    //endregion

    Ok(())
}
