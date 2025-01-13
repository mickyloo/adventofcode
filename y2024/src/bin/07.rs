use adv_code_2024::*;
use anyhow::*;
use code_timing_macros::time_snippet;
use const_format::concatcp;
use itertools::{repeat_n, Itertools};
use std::fs::File;
use std::io::{BufRead, BufReader};

const DAY: &str = "07";
const INPUT_FILE: &str = concatcp!("input/", DAY, ".txt");

const TEST: &str = "\
190: 10 19
3267: 81 40 27
83: 17 5
156: 15 6
7290: 6 8 6 15
161011: 16 10 13
192: 17 8 14
21037: 9 7 18 13
292: 11 6 16 20";

#[derive(Debug, Clone)]
struct Equation {
    total: usize,
    inputs: Vec<usize>,
}

fn main() -> Result<()> {
    start_day(DAY);

    //region Part 1
    println!("=== Part 1 ===");

    fn parse<R: BufRead>(reader: R) -> Vec<Equation> {
        let equations = reader
            .lines()
            .map(|l| {
                let binding = l.unwrap();
                let mut parts = binding.split(": ");
                let total = parts.next().unwrap().parse::<usize>().unwrap();
                let inputs = parts
                    .next()
                    .unwrap()
                    .split(' ')
                    .map(|l| l.parse::<usize>().unwrap())
                    .collect_vec();

                Equation { total, inputs }
            })
            .collect_vec();
        equations
    }

    fn valid_add_mul(equation: &Equation) -> bool {
        let op_combinations =
            repeat_n(vec!['+', '*'], equation.inputs.len() - 1).multi_cartesian_product();
        for combo in op_combinations {
            let result = equation.inputs.iter().enumerate().fold(0, |acc, (i, val)| {
                return if i == 0 {
                    acc + val
                } else {
                    let step = match combo[i - 1] {
                        '+' => acc + val,
                        '*' => acc * val,
                        _ => acc,
                    };
                    step
                };
            });

            if result == equation.total {
                return true;
            }
        }
        false
    }

    fn valid_add_mul_concat(equation: &Equation) -> bool {
        let op_combinations =
            repeat_n(vec!['+', '*', '|'], equation.inputs.len() - 1).multi_cartesian_product();
        for combo in op_combinations {
            let result = equation.inputs.iter().enumerate().fold(0, |acc, (i, val)| {
                return if i == 0 {
                    acc + val
                } else {
                    let step = match combo[i - 1] {
                        '+' => acc + val,
                        '*' => acc * val,
                        '|' => (acc.to_string() + &*val.to_string())
                            .parse::<usize>()
                            .unwrap(),
                        _ => acc,
                    };
                    step
                };
            });

            if result == equation.total {
                return true;
            }
        }
        false
    }

    fn part1<R: BufRead>(reader: R) -> Result<usize> {
        let equations = parse(reader);

        let mut answer: usize = 0;
        for equation in equations {
            let is_valid = valid_add_mul(&equation);
            if is_valid {
                answer += equation.total;
            }
        }

        Ok(answer)
    }

    assert_eq!(3749, part1(BufReader::new(TEST.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part1(input_file)?);
    println!("Result = {}", result);
    //endregion

    //region Part 2
    println!("\n=== Part 2 ===");

    fn part2<R: BufRead>(reader: R) -> Result<usize> {
        let equations = parse(reader);

        let mut answer: usize = 0;
        for equation in equations {
            if valid_add_mul(&equation) || valid_add_mul_concat(&equation) {
                answer += equation.total;
            }
        }

        Ok(answer)
    }

    assert_eq!(11387, part2(BufReader::new(TEST.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part2(input_file)?);
    println!("Result = {}", result);
    // //endregion

    Ok(())
}
