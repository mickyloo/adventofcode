use adv_code_2024::*;
use anyhow::*;
use code_timing_macros::time_snippet;
use const_format::concatcp;
use itertools::Itertools;
use std::fs::File;
use std::io::{BufRead, BufReader};

const DAY: &str = "17"; // TODO: Fill the day
const INPUT_FILE: &str = concatcp!("input/", DAY, ".txt");

const TEST: &str = "\
Register A: 729
Register B: 0
Register C: 0

Program: 0,1,5,4,3,0";

struct CPU {
    a: usize,
    b: usize,
    c: usize,
}

fn main() -> Result<()> {
    start_day(DAY);

    //region Part 1
    println!("=== Part 1 ===");

    fn parse<R: BufRead>(reader: R) -> (CPU, Vec<usize>) {
        let lines = reader.lines().map(|x| x.unwrap()).collect_vec();
        let (_, a) = lines[0].split_once(": ").unwrap();
        let (_, b) = lines[1].split_once(": ").unwrap();
        let (_, c) = lines[2].split_once(": ").unwrap();
        let (_, p) = lines[4].split_once(": ").unwrap();

        let cpu = CPU {
            a: a.to_owned().parse::<usize>().unwrap(),
            b: b.to_owned().parse::<usize>().unwrap(),
            c: c.to_owned().parse::<usize>().unwrap(),
        };

        let program = p
            .split(",")
            .map(|x| x.parse::<usize>().unwrap())
            .collect_vec();
        (cpu, program)
    }

    fn part1<R: BufRead>(reader: R) -> Result<String> {
        let (mut cpu, program) = parse(reader);
        let mut pointer = 0_usize;

        let mut output = vec![];

        while pointer < program.len() {
            let mut is_jump = false;
            let combo_operand = match program[pointer + 1] {
                0 | 1 | 2 | 3 => program[pointer + 1],
                4 => cpu.a,
                5 => cpu.b,
                6 => cpu.c,
                _ => 7,
            };

            match program[pointer] {
                0 => cpu.a = cpu.a >> combo_operand,
                1 => cpu.b = cpu.b ^ program[pointer + 1],
                2 => cpu.b = combo_operand & 7,
                3 => {
                    if cpu.a != 0 {
                        pointer = program[pointer + 1];
                        is_jump = true;
                    }
                }
                4 => cpu.b = cpu.b ^ cpu.c,
                5 => output.push(combo_operand & 7),
                6 => cpu.b = cpu.a >> combo_operand,
                7 => cpu.c = cpu.a >> combo_operand,
                _ => {}
            }

            if !is_jump {
                pointer += 2;
            }
        }

        let answer = output.iter().map(|x| x.to_string()).join(",");
        Ok(answer)
    }

    // TODO: Set the expected answer for the test input
    assert_eq!(
        "4,6,3,5,6,3,5,2,1,0",
        part1(BufReader::new(TEST.as_bytes()))?
    );

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
